package net.junaoki.fflo.action;

public class AjaxResponseResult {
	
	private ResponseResultEnum code;

	public enum ResponseResultEnum{
		Success
		, SessionExpired
		, DbError
	}

	public void setCode(ResponseResultEnum code) { this.code = code; }
	public ResponseResultEnum getCode() { return code; }
}
