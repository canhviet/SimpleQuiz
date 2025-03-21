package viet.DACN.dto.response;

@SuppressWarnings("rawtypes")
public class ResponseError extends ResponseData {
    public ResponseError(int status, String messgae) {
        super(status, messgae);
    }
}
