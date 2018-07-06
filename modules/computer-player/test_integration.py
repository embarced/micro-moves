import stockfish

def test_calculate_move_pawn_captures():
    fen = "2k5/8/8/8/8/3p4/2P5/7K w - - 0 1"
    best_move = stockfish.calculate_move(fen)
    assert best_move == "c2d3"

def test_calculate_move_scholars_mate():
    fen = "r1bqkb1r/pppp1ppp/2n2n2/4p2Q/2B1P3/8/PPPP1PPP/RNB1K1NR w KQkq - 0 3"
    best_move = stockfish.calculate_move(fen)
    assert best_move == "h5f7"
