package com.sego.mvc.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.richitec.util.JSONUtil;
import com.sego.mvc.model.bean.SystemMessages;

@Controller
@RequestMapping("/systeminfo")
public class SystemInfoController {

	@RequestMapping("/getnewnotice")
	public void getNewSystemMsg(HttpServletResponse response,
			@RequestParam(value = "username") String userName,
			@RequestParam(value = "lastmsgid", defaultValue = "0") String lastMsgId) throws IOException {
		SystemMessages messages = new SystemMessages();
		
		response.getWriter().print(JSONUtil.toString(messages));
	}
	
}
