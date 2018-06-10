package org.flexess.games.rulesclient;

public class ValidateMoveResult {

    private boolean valid;

    private String resultingFen;

    private boolean checkmateAfterMove;

    private boolean stalemateAfterMove;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
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
}
