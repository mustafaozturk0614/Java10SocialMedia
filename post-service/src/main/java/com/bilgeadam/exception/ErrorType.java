package com.bilgeadam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    INTERNAL_ERROR_SERVER(5200,"Sunucu Hatası",HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(4200,"Parametre hatası",HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(4210, "Böyle bir kullanıcı bulunamadı",HttpStatus.NOT_FOUND),
    INVALID_CODE(4212, "Geçersiz Kod",HttpStatus.BAD_REQUEST),
    UNEXPECTED_ERROR(4213,"Beklenmeyen bir hata olustu",HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(4214, "Geçersiz Token !!!",HttpStatus.BAD_REQUEST),
    TOKEN_NOT_CREATED(4215, "Token Oluşturulamadı !!!",HttpStatus.BAD_REQUEST),
    POST_NOT_CREATED(4216, "Post olusturulamadı",HttpStatus.BAD_REQUEST ),

    ;


    private int code;
    private String message;
    HttpStatus httpStatus;


}
