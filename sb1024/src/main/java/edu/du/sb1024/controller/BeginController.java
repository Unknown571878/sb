package edu.du.sb1024.controller;

import edu.du.sb1024.board.dto.BoardDto;
import edu.du.sb1024.board.dto.BoardFileDto;
import edu.du.sb1024.board.service.BoardService;
import edu.du.sb1024.entity.Member;
import edu.du.sb1024.repository.MemberRepository;
import edu.du.sb1024.spring.DuplicateMemberException;
import edu.du.sb1024.spring.MemberRegisterService;
import edu.du.sb1024.spring.RegisterRequest;
import edu.du.sb1024.survey.AnsweredData;
import edu.du.sb1024.survey.Question;
import edu.du.sb1024.survey.SurveyService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Controller
@Log4j2
public class BeginController {

    @Autowired
    private BoardService boardService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberRegisterService memberRegisterService;

    @Autowired
    SurveyService surveyService;

    @RequestMapping("/board/boardList")
    public ModelAndView openBoardList() throws Exception{
        log.info("====> openBoardList {}", "테스트");
        ModelAndView mv = new ModelAndView("board/boardList");

        List<BoardDto> list = boardService.selectBoardList();
        mv.addObject("list", list);

        return mv;
    }

    @RequestMapping("board/openBoardWrite.do")
    public String openBoardWrite() throws Exception{
        return "board/boardWrite";
    }

    @RequestMapping("board/insertBoard.do")
    public String insertBoard(BoardDto board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception{
        boardService.insertBoard(board, multipartHttpServletRequest);
        return "redirect:/board/boardList";
    }

    @RequestMapping("board/openBoardDetail.do")
    public ModelAndView openBoardDetail(@RequestParam int boardIdx) throws Exception{
        ModelAndView mv = new ModelAndView("board/boardDetail");

        BoardDto board = boardService.selectBoardDetail(boardIdx);
        mv.addObject("board", board);

        return mv;
    }

    @RequestMapping("board/updateBoard.do")
    public String updateBoard(BoardDto board) throws Exception{
        boardService.updateBoard(board);
        return "redirect:/board/boardList";
    }

    @RequestMapping("board/deleteBoard.do")
    public String deleteBoard(int boardIdx) throws Exception{
        boardService.deleteBoard(boardIdx);
        return "redirect:/board/boardList";
    }

    @RequestMapping("board/downloadBoardFile.do")
    public void downloadBoardFile(@RequestParam int idx, @RequestParam int boardIdx, HttpServletResponse response) throws Exception{
        String currentPath = Paths.get("").toAbsolutePath().toString();
        System.out.println("---------------------"+currentPath);
        BoardFileDto boardFile = boardService.selectBoardFileInformation(idx, boardIdx);
        if(ObjectUtils.isEmpty(boardFile) == false) {
            String fileName = boardFile.getOriginalFileName();

            byte[] files = FileUtils.readFileToByteArray(new File("./src/main/resources/static"+boardFile.getStoredFilePath()));

            response.setContentType("application/octet-stream");
            response.setContentLength(files.length);
            response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(fileName,"UTF-8")+"\";");
            response.setHeader("Content-Transfer-Encoding", "binary");

            response.getOutputStream().write(files);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
    }

    @GetMapping("/survey/surveyForm")
    public String form(Model model) {
        List<Question> questions = createQuestions();
        for (Question question : questions) {
            System.out.println(question);
        }
        model.addAttribute("questions", questions);
        return "/survey/surveyForm";
    }

    private List<Question> createQuestions() {
        Question q1 = new Question("당신의 역할은 무엇입니까?",
                Arrays.asList("서버", "프론트", "풀스택"));
        Question q2 = new Question("많이 사용하는 개발도구는 무엇입니까?",
                Arrays.asList("이클립스", "인텔리J", "서브라임"));
        Question q3 = new Question("하고 싶은 말을 적어주세요.");
        return Arrays.asList(q1, q2, q3);
    }

    @PostMapping("/survey/submitted")
    public String submit(@ModelAttribute("ansData") AnsweredData data) {


        surveyService.save(data);
        return "/survey/submitted";
    }

    @GetMapping("/")
    public String index() {
        return "/sample/all";
    }

    @RequestMapping("/register/step1")
    public String handleStep1() {
        return "register/step1";
    }

    @GetMapping("/register/step2")
    public String handleStep2(
            @RequestParam(value = "agree", defaultValue = "false") Boolean agree,
            Model model) {
        if (!agree) {
            return "register/step1";
        }
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register/step2";
    }

//    @GetMapping("/register/step2")
//    public String handleStep2Get() {
//        return "redirect:/register/step1";
//    }

    @GetMapping("/register/step3")
    public String handleStep3(RegisterRequest regReq) {
        try {
            memberRegisterService.regist(regReq);
            return "register/step3";
        } catch (DuplicateMemberException ex) {
            return "register/step2";
        }
    }

//    @GetMapping("/login")
//    public String loginForm() {
//        return "/sample/loginForm";
//    }
//
//    @PostMapping("/login")
//    public String loginResult(@RequestParam String username, @RequestParam String password, Model model) {
//        log.info("이메일 {}, 패스워드 {}", username, password);
//        return "/";
//    }

    @PostConstruct
    public void init() {
        Member member = Member.builder()
                .id(1001L)
                .username("hong1")
                .password(passwordEncoder().encode("1234"))
                .email("hong1@aaa.com")
                .role("ADMIN")
                .build();
        memberRepository.save(member);

        member = Member.builder()
                .id(1002L)
                .username("test1")
                .password(passwordEncoder().encode("1234"))
                .email("test1@aaa.com")
                .role("USER")
                .build();
        memberRepository.save(member);
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
