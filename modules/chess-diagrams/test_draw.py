import draw


# create an image and check dimension and color
def test_create_image():
    img = draw.create_image(200, 100, color='red')

    assert img.width == 200
    assert img.height == 100

    color_list = img.getcolors()
    assert len(color_list) == 1

    first_color = color_list[0]
    assert first_color[0] == 200 * 100 # count
    assert first_color[1] == (255, 0, 0, 255) # pixel (red)


def test_draw_diagram_for_fen():
    initial_pos_fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
    img = draw.draw_diagram_for_fen(initial_pos_fen)
    assert not img == None
