package utils;

public class ABMResult {
    final boolean result;
    final String resultMessage;

    public ABMResult(boolean result, String resultMessage) {
        this.result = result;
        this.resultMessage = resultMessage;
    }

    public boolean getResult() {
        return result;
    }

    public String getResultMessage() {
        return resultMessage;
    }
}
