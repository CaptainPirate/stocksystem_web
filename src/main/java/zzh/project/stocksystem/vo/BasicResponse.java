package zzh.project.stocksystem.vo;

import zzh.project.stocksystem.ErrorCode;

public class BasicResponse {
	public int errcode = ErrorCode.SUCCESS;
	public Object data;
	public String errmsg = "";
	
	@Override
	public String toString() {
		return "BasicResponse [errcode=" + errcode + ", data=" + data + ", errmsg=" + errmsg + "]";
	}
}
