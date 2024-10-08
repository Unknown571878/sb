package edu.du.chap17;

import edu.du.chap17.dao.ArticleDao;
import edu.du.chap17.model.Article;
import edu.du.chap17.model.ArticleListModel;
import edu.du.chap17.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MyController {

    @Autowired
    ListArticleService listSerivce;

    @Autowired
    ReadArticleService readSerivce;

    @Autowired
    WriteArticleService writeSerivce;

    @GetMapping("/")
    public String root() {
        return "redirect:/list";
    }

    @GetMapping("/list")
    public String list(HttpServletRequest request, Model model) {
        String pageNumberString = request.getParameter("p");
        int pageNumber = 1;
        if (pageNumberString != null && pageNumberString.length() > 0) {
            pageNumber = Integer.parseInt(pageNumberString);
        }
        ArticleListModel articleListModel = listSerivce.getArticleList(pageNumber);
        request.setAttribute("listModel", articleListModel);

        if (articleListModel.getTotalPageCount() > 0) {
            int beginPageNumber =
                    (articleListModel.getRequestPage() - 1) / 10 * 10 + 1;
            int endPageNumber = beginPageNumber + 9;
            if (endPageNumber > articleListModel.getTotalPageCount()) {
                endPageNumber = articleListModel.getTotalPageCount();
            }
            request.setAttribute("beginPage", beginPageNumber);
            request.setAttribute("endPage", endPageNumber);
        }
        return "list_view";
    }

    @GetMapping("/read")
    public String read(HttpServletRequest request, Model model) {
        int articleId = Integer.parseInt(request.getParameter("articleId"));
        String viewPage = null;
        try {
            Article article = readSerivce.getArticle(articleId);
            request.setAttribute("article", article);
            viewPage = "read_view";
        } catch(ArticleNotFoundException ex) {
            viewPage = "article_not_found";
        }

        return viewPage;
    }

    @GetMapping("/writeForm")
    public String writeForm() {
        return "writeForm";
    }

    @PostMapping("/write")
    public String write(WritingRequest writingRequest) throws IdGenerationFailedException {
        writeSerivce.write(writingRequest);
        return "redirect:/list";
    }
}
