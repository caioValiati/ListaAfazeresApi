package com.example.controller.util;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.function.Supplier;
import com.example.dto.BaseResponse;
import com.example.dto.BaseResponse;


public class ResponseHandler {

   public static <T extends BaseResponse> ResponseEntity<T> handleServiceCall(
        Supplier<T> serviceCall, 
        HttpStatus... successFailStatus) {
    
        try {
            T response = serviceCall.get();

            if (response.isSuccess()) {
                HttpStatus successStatus = successFailStatus.length > 0 ? successFailStatus[0] : HttpStatus.OK;
                return ResponseEntity.status(successStatus).body(response);
            } else {
                HttpStatus failStatus = successFailStatus.length > 1 ? successFailStatus[1] : HttpStatus.CONFLICT;
                return ResponseEntity.status(failStatus).body(response);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse(e.getMessage()));
        }
    }


    @SuppressWarnings("unchecked")
    public static <T extends BaseResponse> T createErrorResponse(String message) {
        return (T) new BaseResponse(false, message);
    }
}
