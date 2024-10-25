package edu.du.sb1021_2.controller;

import edu.du.sb1021_2.spring.DuplicateMemberException;
import edu.du.sb1021_2.spring.MemberDao;
import edu.du.sb1021_2.spring.MemberRegisterService;
import edu.du.sb1021_2.spring.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class RegisterController {

	final MemberRegisterService memberRegisterService;
	
	@GetMapping("/")
	public String root() {
		return "redirect:/register/step1";
	}

	@RequestMapping("/register/step1")
	public String handleStep1() {
		return "register/step1";
	}

	@PostMapping("/register/step2")
	public String handleStep2(
			@RequestParam(value = "agree", defaultValue = "false") Boolean agree,
			Model model) {
		if (!agree) {
			return "register/step1";
		}
		model.addAttribute("registerRequest", new RegisterRequest());
		return "register/step2";
	}

	@GetMapping("/register/step2")
	public String handleStep2Get() {
		return "redirect:/register/step1";
	}

	@PostMapping("/register/step3")
	public String handleStep3(RegisterRequest regReq, Model model) {
		try {
			memberRegisterService.regist(regReq);
			model.addAttribute("registerResponse", regReq.getName());
			return "register/step3";
		} catch (DuplicateMemberException ex) {
			return "register/step2";
		}
	}

}
