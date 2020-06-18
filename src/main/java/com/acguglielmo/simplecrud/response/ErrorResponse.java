package com.acguglielmo.simplecrud.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

	private String message;

}
