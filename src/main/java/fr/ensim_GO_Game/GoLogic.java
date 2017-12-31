package fr.ensim_GO_Game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class GoLogic implements InterfaceGoLogic  {

		private GoPiece[][] board;
	    private int player1_score, player2_score;
	    private int move = 1;
	    private boolean gameOver = false;

	    GoLogic(GoPiece[][] board) {
	        this.board = board;
	        player1_score = 0;
	        player2_score = 0;
	    }

	    public void placePiece(int x, int y, int player) throws Exception{
	        if(gameOver) throw new Exception("Game is over");

	        if(!getPiece(x, y).isEmpty()) throw new Exception("Place is taken");

	        GoPiece selectedPiece = getPiece(x, y);
	        Set<GoPiece> patch = buildPatch(selectedPiece, player);


	        if(isSuicideMove(selectedPiece, patch, player)){
	            if(isKOMove(selectedPiece, patch, player)){
	                selectedPiece.setPiece(player);
	            } else throw new Exception("This is suicide move");
	        } else {
	            selectedPiece.setPiece(player);
	        }

	        takeOpponentPieces(selectedPiece, player);
	        move++;
	        for(GoPiece[] row: board)
	            for(GoPiece piece: row)
	                piece.setForMoveLevel(move);

	        if(isRepeatableState()) {
	            undo();
	            throw new Exception("Repeatable state");
	        }
	    }

	    private void undo() {
	        move--;
	        for(GoPiece[] row: board)
	            for(GoPiece piece: row) piece.undoLastMove();
	    }

	    private boolean isRepeatableState() {
	        boolean isRepeatableState = true;
	        for(GoPiece[] row: board)
	            for(GoPiece piece: row) isRepeatableState = piece.isReptableState() != false && isRepeatableState;
	        return isRepeatableState;
	    }

	    private boolean isKOMove(GoPiece selectedPiece, Set<GoPiece> patch, int player) {
	        boolean isKOMove = false;
	        final int other = player == 1 ? 2 : 1;
	        final int x = selectedPiece.getX();
	        final int y = selectedPiece.getY();

	        if(isValidIndex(x - 1, y) && getPiece(x - 1, y).getPiece() == other){
	            isKOMove = canTakeoverArea(getPiece(x - 1, y), selectedPiece) || isKOMove; }

	        if(isValidIndex(x + 1, y) && getPiece(x + 1, y).getPiece() == other){
	            isKOMove = canTakeoverArea(getPiece(x + 1, y), selectedPiece) || isKOMove; }

	        if(isValidIndex(x, y - 1) && getPiece(x, y - 1).getPiece() == other){
	            isKOMove = canTakeoverArea(getPiece(x, y - 1), selectedPiece) || isKOMove; }

	        if(isValidIndex(x, y + 1) && getPiece(x, y + 1).getPiece() == other){
	            isKOMove = canTakeoverArea(getPiece(x, y + 1), selectedPiece) || isKOMove; }

	        return isKOMove;
	    }

	    private boolean canTakeoverArea(GoPiece piece, GoPiece selectedPiece) {
	        return isPatchSurrounded(buildPatch(piece, piece.getPlayer()), selectedPiece);
	    }

	    private boolean isPatchSurrounded(Set<GoPiece> goPieces, GoPiece selectedPiece) {
	        boolean isSurrounded = true;
	        for(GoPiece piece: goPieces) {
	            final int x = piece.getX();
	            final int y = piece.getY();
	            if(isValidIndex(x - 1, y) && getPiece(x - 1, y).getPiece() == 0 && getPiece(x - 1, y) != selectedPiece){ isSurrounded = false; }
	            if(isValidIndex(x + 1, y) && getPiece(x + 1, y).getPiece() == 0 && getPiece(x + 1, y) != selectedPiece){ isSurrounded = false; }
	            if(isValidIndex(x, y - 1) && getPiece(x, y - 1).getPiece() == 0 && getPiece(x, y - 1) != selectedPiece){ isSurrounded = false; }
	            if(isValidIndex(x, y + 1) && getPiece(x, y + 1).getPiece() == 0 && getPiece(x, y + 1) != selectedPiece){ isSurrounded = false; }
	        }
	        return isSurrounded;
	    }

	    private void takeOpponentPieces(GoPiece selectedPiece, int player) {
	        final int other = player == 1 ? 2 : 1;
	        final int x = selectedPiece.getX();
	        final int y = selectedPiece.getY();
	        if(isValidIndex(x - 1, y) && getPiece(x - 1, y).getPiece() == other){ takeOverIfSurrounded(getPiece(x - 1, y), player); }
	        if(isValidIndex(x + 1, y) && getPiece(x + 1, y).getPiece() == other){ takeOverIfSurrounded(getPiece(x + 1, y), player); }
	        if(isValidIndex(x, y - 1) && getPiece(x, y - 1).getPiece() == other){ takeOverIfSurrounded(getPiece(x, y - 1), player); }
	        if(isValidIndex(x, y + 1) && getPiece(x, y + 1).getPiece() == other){ takeOverIfSurrounded(getPiece(x, y + 1), player); }
	    }

	    private void takeOverIfSurrounded(GoPiece startPiece, int player) {
	        Set<GoPiece> patch = buildPatch(startPiece, startPiece.getPiece());
	        if(isPatchSurrounded(patch)){
	            updateScore(player == 1 ? 1 : 2, patch.size());
	            for(GoPiece piece: patch) piece.setPiece(0);
	        }
	    }

	    private void updateScore(int player, int increment) {
	        if(player == 1) player1_score += increment;
	        else player2_score += increment;
	    }

	    private boolean isPatchSurrounded(Set<GoPiece> patch) {
	        boolean isSurrounded = true;
	        for(GoPiece piece: patch) {
	            final int x = piece.getX();
	            final int y = piece.getY();
	            if(isValidIndex(x - 1, y) && getPiece(x - 1, y).getPiece() == 0) { isSurrounded = false; break; }
	            if(isValidIndex(x + 1, y) && getPiece(x + 1, y).getPiece() == 0) { isSurrounded = false; break; }
	            if(isValidIndex(x, y - 1) && getPiece(x, y - 1).getPiece() == 0) { isSurrounded = false; break; }
	            if(isValidIndex(x, y + 1) && getPiece(x, y + 1).getPiece() == 0) { isSurrounded = false; break; }
	        }
	        return isSurrounded;
	    }

	    private boolean hasEscapeRoute(GoPiece currentPiece, Set<GoPiece> patch, GoPiece selectedPiece){
	        boolean hasEscapeRoute = false;
	        for(GoPiece piece: patch) {
	            hasEscapeRoute = hasAvailableAdjacent(piece, currentPiece, selectedPiece) || hasEscapeRoute;
	        }
	        return hasEscapeRoute;
	    }

	    private boolean hasAvailableAdjacent(GoPiece piece, GoPiece origin, GoPiece selectedPiece) {
	        int x = piece.getX();
	        int y = piece.getY();
	        boolean hasAvailableAdjacent = false;
	        if(isValidIndex(x - 1, y) && isAvailablePlace(getPiece(x - 1, y), selectedPiece)) { hasAvailableAdjacent = true; }
	        if(isValidIndex(x + 1, y) && isAvailablePlace(getPiece(x + 1, y), selectedPiece)) { hasAvailableAdjacent = true; }
	        if(isValidIndex(x, y - 1) && isAvailablePlace(getPiece(x, y - 1), selectedPiece)) { hasAvailableAdjacent = true; }
	        if(isValidIndex(x, y + 1) && isAvailablePlace(getPiece(x, y + 1), selectedPiece)) { hasAvailableAdjacent = true; }
	        return hasAvailableAdjacent;
	    }

	    private boolean isAvailablePlace(GoPiece piece, GoPiece origin) {
	        return piece.getPiece() == 0 && piece != origin;
	    }

	    private boolean isSuicideMove(GoPiece selectedPiece, Set<GoPiece> patch, int player){
	        boolean hasEscape = false;
	        for(GoPiece piece: patch) {
	            hasEscape = hasEscapeRoute(piece, patch, selectedPiece) || hasEscape;
	        }
	        return !hasEscape;
	    }

	    private Set<GoPiece> buildPatch(GoPiece origin, int player) {
	        Set<GoPiece> patch = new HashSet<GoPiece>();
	        buildPatch(origin, patch, player);
	        return patch;
	    }

	    private void buildPatch(GoPiece origin, Set<GoPiece> patch, int player) {
	        if(patch.contains(origin)) return;
	        patch.add(origin);
	        int x = origin.getX();
	        int y = origin.getY();

	        if(isValidIndex(x - 1, y) && getPiece(x - 1, y).getPlayer() == player) {
	            buildPatch(getPiece(x - 1, y), patch, player);
	        }

	        if(isValidIndex(x + 1, y) && getPiece(x + 1, y).getPlayer() == player) {
	            buildPatch(getPiece(x + 1, y), patch, player);
	        }

	        if(isValidIndex(x, y - 1) && getPiece(x, y - 1).getPlayer() == player) {
	            buildPatch(getPiece(x, y - 1), patch, player);
	        }

	        if(isValidIndex(x, y + 1) && getPiece(x, y + 1).getPlayer() == player) {
	            buildPatch(getPiece(x, y + 1), patch, player);
	        }
	    }

	    private boolean isValidIndex(int x, int y) {
	        return x >= 0 && y >= 0 && x < board.length && y < board[0].length;
	    }

	    public GoPiece getPiece(int x, int y) {
	        return board[x][y];
	    }


	    private void addPatchToScore(List<GoPiece> patch){
	        boolean isConnectedToPlayerOne = false;
	        boolean isConnectedToPlayerTwo = false;
	        for(GoPiece piece: patch) {
	            isConnectedToPlayerOne = isConnectedToPlayer(piece, 1) || isConnectedToPlayerOne;
	            isConnectedToPlayerTwo = isConnectedToPlayer(piece, 2) || isConnectedToPlayerTwo;
	        }

	        if(!isConnectedToPlayerOne && isConnectedToPlayerTwo) player2_score += patch.size();
	        if(isConnectedToPlayerOne && !isConnectedToPlayerTwo) player1_score += patch.size();
	    }

	    private boolean isConnectedToPlayer(GoPiece piece, int player) {
	        boolean isConnectedToPlayer = false;
	        final int x = piece.getX();
	        final int y = piece.getY();
	        if(isValidIndex(x - 1, y) && getPiece(x - 1, y).getPiece() == player) isConnectedToPlayer = true;
	        if(isValidIndex(x + 1, y) && getPiece(x + 1, y).getPiece() == player) isConnectedToPlayer = true;
	        if(isValidIndex(x, y - 1) && getPiece(x, y - 1).getPiece() == player) isConnectedToPlayer = true;
	        if(isValidIndex(x, y + 1) && getPiece(x, y + 1).getPiece() == player) isConnectedToPlayer = true;
	        return isConnectedToPlayer;
	    }

	    private List<List<GoPiece>> buildFreePatches() {
	        Set<GoPiece> checkedFreePieces = new HashSet<GoPiece>();
	        List<List<GoPiece>> patches = new ArrayList<List<GoPiece>>();

	        for(GoPiece[] row: board)
	            for(GoPiece piece: row)
	                if(piece.getPiece() == 0 && !checkedFreePieces.contains(piece))
	                    patches.add(buildFreePatch(checkedFreePieces, piece));
	        return patches;
	    }

	    private List<GoPiece> buildFreePatch(Set<GoPiece> checkedFreePieces, GoPiece startPiece) {
	        List<GoPiece> patch = new ArrayList<GoPiece>();
	        if(checkedFreePieces.contains(startPiece)) return patch;
	        checkedFreePieces.add(startPiece);
	        patch.add(startPiece);
	        final int x = startPiece.getX();
	        final int y = startPiece.getY();
	        if(isValidIndex(x - 1, y) && getPiece(x - 1, y).getPiece() == 0) patch.addAll(buildFreePatch(checkedFreePieces, getPiece(x - 1, y)));
	        if(isValidIndex(x + 1, y) && getPiece(x + 1, y).getPiece() == 0) patch.addAll(buildFreePatch(checkedFreePieces, getPiece(x + 1, y)));
	        if(isValidIndex(x, y - 1) && getPiece(x, y - 1).getPiece() == 0) patch.addAll(buildFreePatch(checkedFreePieces, getPiece(x, y - 1)));
	        if(isValidIndex(x, y + 1) && getPiece(x, y + 1).getPiece() == 0) patch.addAll(buildFreePatch(checkedFreePieces, getPiece(x, y + 1)));
	        checkedFreePieces.addAll(patch);
	        return patch;
	    }

	    public void endGame() {
	        List<List<GoPiece>> patches = buildFreePatches();
	        for(List<GoPiece> patch: patches) addPatchToScore(patch);
	        gameOver = true;
	    }

	    // private method that determines who won the game
	    public String determineWinner() {
	        if(player1_score > player2_score) {
	            return "Black wins!";
	        }else if(player1_score < player2_score) {
	            return "White wins!";
	        }else {
	            return "Draw";
	        }

	    }

	    public int playerOneScore() {
	        return player1_score;
	    }

	    public int playerTwoScore() {
	        return player2_score;
	    }


}
