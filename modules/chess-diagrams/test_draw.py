import draw

def test_create_image():
    img = draw.create_image(100, 100, color='green')
    assert img.width == 100
    assert img.height == 100


def test_draw_diagram_for_fen():
    img = draw.draw_diagram_for_fen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
    assert not img == None
