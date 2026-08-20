import java.util.ArrayDeque;
import java.util.Deque;

class BrowserHistory {
    private Deque<String> history = new ArrayDeque<>();

    void visit(String url) {
        if (url == null || url.isEmpty()) {
            System.out.println("visit 失敗：網址不可為空");
            return;
        }
        history.push(url);
        System.out.println("visit " + url + " -> 目前頁面：" + current());
    }

    String back() {
        if (history.size() <= 1) {
            System.out.println("back 失敗：沒有可返回的上一頁");
            return current();
        }
        String leaving = history.pop();
        System.out.println("back 離開 " + leaving + " -> 目前頁面：" + current());
        return current();
    }

    String current() {
        return history.isEmpty() ? "(空白頁)" : history.peek();
    }

    int size() {
        return history.size();
    }
}

public class BrowserBackStack {
    public static void main(String[] args) {
        BrowserHistory browser = new BrowserHistory();

        System.out.println("起始頁面：" + browser.current());
        browser.back();

        browser.visit("tku.edu.tw");
        browser.visit("dev.java");
        browser.visit("docs.oracle.com");
        System.out.println("歷程筆數：" + browser.size());

        browser.back();
        browser.back();
        browser.back();
        browser.back();

        browser.visit("");
        browser.visit("moodle.tku.edu.tw");

        System.out.println("最終頁面：" + browser.current());
        System.out.println("最終歷程筆數：" + browser.size());
    }
}
