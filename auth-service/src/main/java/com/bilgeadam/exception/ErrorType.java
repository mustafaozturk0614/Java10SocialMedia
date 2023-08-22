package com.bilgeadam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    INTERNAL_ERROR_SERVER(5100,"Sunucu Hatası",HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(4100,"Parametre hatası",HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(4110, "Böyle bir kullanıcı bulunamadı",HttpStatus.NOT_FOUND),
    ACCOUNT_NOT_ACTIVE(4111, "Hesabınız Aktif değildir",HttpStatus.BAD_REQUEST),
    INVALID_CODE(4112, "Geçersiz Kod",HttpStatus.BAD_REQUEST),
    ALREADY_ACTIVE(4113, "Hesabınız zaten aktif",HttpStatus.BAD_REQUEST),
    UNEXPECTED_ERROR(4114,"Beklenmeyen bir hata olustu",HttpStatus.BAD_REQUEST),


    ;

    private int code;
    private String message;
    HttpStatus httpStatus;


}
