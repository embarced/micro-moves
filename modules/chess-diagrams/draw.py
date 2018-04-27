import PIL.Image
import PIL.ImageDraw
import PIL.ImageFont


SQUARE_SIZE = 32
BORDER_SIZE = int(SQUARE_SIZE / 2)
BOARD_SIZE = int(SQUARE_SIZE * 8 + 2 * BORDER_SIZE)


BOARD_COLOR_LIGHT = 'white'
BOARD_COLOR_DARK = 'lightgray'
BOARD_COLOR_BORDER = 'darkgray'
BOARD_COLOR_KEY = 'white'


FONT_PATH = 'fonts/'
IMAGE_PATH = 'images/pieces/32'
ALL_PIECES = ( 'bb', 'bk', 'bn', 'bp', 'bq', 'br', 'wb', 'wk', 'wn', 'wp', 'wq', 'wr')


piece_images = {}
for piece in ALL_PIECES:
    filename = IMAGE_PATH + '/' + piece + '.png'
    image = PIL.Image.open(filename)
    piece_images[piece] = image


def create_image (width, height, color='white'):
    """
    Creates an empty image.

    :param width: image width
    :param height: image height
    :param color: background color
    :return: the image
    """

    image = PIL.Image.new("RGBA", (width, height), color)
    return image


def draw_board (image, square_size, start_x, start_y, light=BOARD_COLOR_LIGHT, dark=BOARD_COLOR_DARK):
    """
    Draw a chequered 8x8 chess board into a given images.

    :param image: target image
    :param square_size: size of a single square (width and height)
    :param start_x: x position of upper left corner of board
    :param start_y: y position of upper left corner of board
    :param light: light square color
    :param dark: dark square color
    """

    img_draw = PIL.ImageDraw.Draw(image)
    x = 0
    y = 0
    for square in range(64):
        rect = (start_x + x * square_size, start_y + y * square_size, start_x + x * square_size + square_size, start_y + y * square_size + square_size)
        if (x+y) % 2 == 0:
            img_draw.rectangle(rect, fill=light)
        else :
            img_draw.rectangle(rect, fill=dark)
        x += 1
        if x == 8:
            y += 1
            x = 0
    return image


def draw_key(image, square_size, start_x, start_y):
    """
    Draw a key (a..h, 1..8) for the squares into a given images.

    :param image: target image
    :param square_size: size of a single square (width and height)
    :param start_x: x position of upper left corner of board
    :param start_y: y position of upper left corner of board
    """

    font_size = int(square_size / 2.5)
    font_dy = int((start_y - font_size) / 2)
    font_dx = int((start_x - font_size/2)/2)

    font = PIL.ImageFont.truetype(FONT_PATH + 'arial.ttf', font_size)
    img_draw = PIL.ImageDraw.Draw(image)

    numbers = "87654321"
    pos_x_1 = font_dx
    pos_x_2 = font_dx + 8.5 * square_size
    pos_y = start_y + int(square_size / 6)
    for number in numbers:
        img_draw.text((pos_x_1, pos_y), number, font=font, fill=BOARD_COLOR_KEY)
        img_draw.text((pos_x_2, pos_y), number, font=font, fill=BOARD_COLOR_KEY)
        pos_y += square_size

    letters = "abcdefgh"
    pos_x = start_x + int (square_size/4) + font_dx
    pos_y_1 = font_dy
    pos_y_2 = start_y + 8 * square_size + font_dy
    for letter in letters:
        img_draw.text((pos_x, pos_y_1), letter, font=font, fill=BOARD_COLOR_KEY)
        img_draw.text((pos_x, pos_y_2), letter, font=font, fill=BOARD_COLOR_KEY)
        pos_x += square_size


def piece_image_for_letter(c):
    if c.islower():
        piece = 'b' + c
    else:
        piece = 'w' + c.lower()
    piece_images.get(piece)


def draw_pieces(image, square_size, start_x, start_y, pieces):
    """
    Draws chess pieces into a given image.

    :param image: target image
    :param square_size: size of a single square (width and height)
    :param start_x: x position of upper left corner of board
    :param start_y: y position of upper left corner of board
    :param pieces: pieces on the bord (1st FEN group)
    """

    rows = pieces.split('/')
    row_no = 0
    line_no  = 0
    for row in rows:
        for c in row:
            if c.isdigit():
                line_no += int(c)
            else:
                piece_image = piece_image_for_letter(piece)
                pos = (start_x + square_size * line_no, start_y + square_size * row_no)
                image.paste(piece_image, pos, piece_image)
                line_no += 1
        row_no += 1
        line_no = 0


def draw_diagram_for_fen(fen):
    """ Creates an image for the given FEN position."""

    image = create_image(BOARD_SIZE, BOARD_SIZE, color=BOARD_COLOR_BORDER)

    draw_board(image, SQUARE_SIZE, BORDER_SIZE, BORDER_SIZE, light=BOARD_COLOR_LIGHT, dark=BOARD_COLOR_DARK)
    draw_key(image, SQUARE_SIZE, BORDER_SIZE, BORDER_SIZE)

    groups = fen.split(" ")
    pieces = groups[0]
    draw_pieces(image, SQUARE_SIZE, BORDER_SIZE, BORDER_SIZE, pieces)

    return image
