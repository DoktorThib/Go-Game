package fr.ensim_GO_Game;


public interface InterfaceGoLogic {

    public void placePiece(int x, int y, int player) throws Exception;

    public GoPiece getPiece(int x, int y);

    public String determineWinner();

    public int playerOneScore();

    public int playerTwoScore();

    public void endGame();

}
