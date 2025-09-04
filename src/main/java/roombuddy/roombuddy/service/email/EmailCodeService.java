package roombuddy.roombuddy.service.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roombuddy.roombuddy.dto.login.SendEmailRequestDto;
import roombuddy.roombuddy.dto.login.ValidateEmailCodeRequestDto;
import roombuddy.roombuddy.exception.ErrorMessage;
import roombuddy.roombuddy.exception.MemberCustomException;
import roombuddy.roombuddy.utils.CodeUtil;

/**
 * 이메일 인증 코드 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailCodeService {

    private final RedisTemplate<String, String> redisTemplate; //redis 템플릿
    private final JavaMailSender mailSender;//JAVA 메일

    /**
     * [서비스 로직]
     * 인증코드 생성 + 이메일 전송
     * @param dto
     */
    @Transactional
    public void sendEmailCode(SendEmailRequestDto dto) {
        //이메일
        String email = dto.getEmail();

        //랜덤 인증 코드 생성
        String code = CodeUtil.createCode();

        //이메일 전송
        sendEmail(email,code);

        //redis에 3분동안 저장
        saveData(email, code);
    }

    @Transactional
    public void validateCode(ValidateEmailCodeRequestDto dto){
        //검증
        if(!verify(dto.getEmail(),dto.getCode())){
            throw new MemberCustomException(ErrorMessage.FAILED_VALIDATE_EMAIL);
        }
    }

    /**
     * [서비스 로직]
     * 메일 전송
     * @param recipient 수신자 이메일
     * @param code 인증 코드
     */
    private void sendEmail(String recipient, String code) {
        try{
            //SimpleMailMessage 객체 생성
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recipient); //수신자 이메일
            message.setSubject("룸버디 인증 코드"); //이메일 제목
            message.setText("인증 코드 : " +code); // 이메일 내용
            message.setFrom("룸버디 <dlwnsgkr8318@gmail.com>");

            //이메일 전송
            mailSender.send(message);
            log.info("이메일 전송 성공!");

        }catch (Exception e){
            log.warn("이메일 전송 실패: {}", e.getMessage());
        }
    }

    /**
     * [서비스 로직]
     * 이메일 인증코드 검증 + 삭제
     * @param email 이메일
     * @param code 인증 코드
     * @return boolean
     */
    @Transactional
    public boolean verify(String email, String code) {
        String saved = getCode(email);
        boolean ok = saved != null && saved.equals(code);
        if (ok) redisTemplate.delete(key(email));
        return ok;
    }

    /**
     * [서비스 로직]
     * redis에 저장
     * @param email 이메일
     * @param code 인증 코드
     */
    @Transactional
    public void saveData(String email, String code ){

        //Redis에 저장
        redisTemplate.opsForValue().set(key(email), code, 3, java.util.concurrent.TimeUnit.MINUTES);
    }

    /**
     * [서비스 로직]
     * 키값으로 조회
     * @param email 이메일
     * @return String
     */
    public String getCode(String email) {
        return redisTemplate.opsForValue().get(key(email));
    }

    //==redis 키값 생성==//
    private String key(String email) {
        return "email:" + email.toLowerCase();
    }

}
