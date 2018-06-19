package org.flexess.games.rulesclient;

public class ValidateMoveResult {

    private boolean valid;

    private boolean validationFailed;

    private String resultingFen;

    private boolean checkmateAfterMove;

    private boolean stalemateAfterMove;

    private String description;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isValidationFailed() {
        return validationFailed;
    }

    public void setValidationFailed(boolean validationFailed) {
        this.validationFailed = validationFailed;
    }

    public String getResultingFen() {
        return resultingFen;
    }

    public void setResultingFen(String resultingFen) {
        this.resultingFen = resultingFen;
    }

    public boolean isCheckmateAfterMove() {
        return checkmateAfterMove;
    }

    public void setCheckmateAfterMove(boolean checkmateAfterMove) {
        this.checkmateAfterMove = checkmateAfterMove;
    }

    public boolean isStalemateAfterMove() {
        return stalemateAfterMove;
    }

    public void setStalemateAfterMove(boolean stalemateAfterMove) {
        this.stalemateAfterMove = stalemateAfterMove;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
