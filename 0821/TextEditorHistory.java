import java.util.ArrayDeque;
import java.util.Deque;

class TextEditor {
    private Deque<String> undoStack = new ArrayDeque<>();
    private Deque<String> redoStack = new ArrayDeque<>();

    void type(String text) {
        if (text == null || text.isEmpty()) {
            System.out.println("輸入失敗：內容不可為空");
            return;
        }
        undoStack.push(text);
        redoStack.clear();
        printState("type " + text);
    }

    void undo() {
        if (undoStack.isEmpty()) {
            System.out.println("undo 失敗：沒有可復原的操作");
            printState("undo(空)");
            return;
        }
        String moved = undoStack.pop();
        redoStack.push(moved);
        printState("undo " + moved);
    }

    void redo() {
        if (redoStack.isEmpty()) {
            System.out.println("redo 失敗：沒有可重做的操作");
            printState("redo(空)");
            return;
        }
        String moved = redoStack.pop();
        undoStack.push(moved);
        printState("redo " + moved);
    }

    String content() {
        StringBuilder sb = new StringBuilder();
        String[] items = undoStack.toArray(new String[0]);
        for (int i = items.length - 1; i >= 0; i--) {
            sb.append(items[i]);
        }
        return sb.length() == 0 ? "(空白)" : sb.toString();
    }

    void printState(String action) {
        System.out.printf("%-12s 內容=%-12s undo=%-20s redo=%s%n",
                action, content(), undoStack, redoStack);
    }
}

public class TextEditorHistory {
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        editor.printState("初始");

        editor.undo();
        editor.redo();

        editor.type("Hello");
        editor.type(" ");
        editor.type("Java");

        editor.undo();
        editor.undo();
        editor.redo();

        System.out.println();
        System.out.println("=== 新增操作後 redo 應被清空 ===");
        editor.type("!");
        editor.redo();

        System.out.println();
        System.out.println("=== 連續 undo 到底 ===");
        editor.undo();
        editor.undo();
        editor.undo();
        editor.undo();

        System.out.println();
        System.out.println("最終內容：" + editor.content());
    }
}
