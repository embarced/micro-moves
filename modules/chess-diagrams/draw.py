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
FONT_FILENAME = 'arial.ttf'
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

    font = PIL.ImageFont.truetype(FONT_PATH + FONT_FILENAME, font_size)
    img_draw = PIL.ImageDraw.Draw(image)

    rank_names = "87654321"
    pos_x_1 = font_dx
    pos_x_2 = font_dx + 8.5 * square_size
    pos_y = start_y + int(square_size / 6)
    for rank in rank_names:
        img_draw.text((pos_x_1, pos_y), rank, font=font, fill=BOARD_COLOR_KEY)
        img_draw.text((pos_x_2, pos_y), rank, font=font, fill=BOARD_COLOR_KEY)
        pos_y += square_size

    file_names = "abcdefgh"
    pos_x = start_x + int (square_size/4) + font_dx
    pos_y_1 = font_dy
    pos_y_2 = start_y + 8 * square_size + font_dy
    for file in file_names:
        img_draw.text((pos_x, pos_y_1), file, font=font, fill=BOARD_COLOR_KEY)
        img_draw.text((pos_x, pos_y_2), file, font=font, fill=BOARD_COLOR_KEY)
        pos_x += square_size


def piece_image_for_letter(c):
    """
    Return a graphic representation for the given piece.
    'K' is a white king, 'p' is a black pawn (lower case means black)
    :param c:
    :return: the image for the piece
    """

    if c.islower():
        piece = 'b' + c
    else:
        piece = 'w' + c.lower()
    return piece_images.get(piece)


def draw_pieces(image, square_size, start_x, start_y, pieces):
    """
    Draws chess pieces into a given image.

    :param image: target image
    :param square_size: size of a single square (width and height)
    :param start_x: x position of upper left corner of board
    :param start_y: y position of upper left corner of board
    :param pieces: pieces on the bord (1st FEN group)
    """

    ranks = pieces.split('/')
    rank_no = 0
    file_no  = 0
    for rank in ranks:
        for character in rank:
            if character.isdigit():
                file_no += int(character)
            else:
                piece_image = piece_image_for_letter(character)
                pos = (start_x + square_size * file_no, start_y + square_size * rank_no)
                image.paste(piece_image, pos, piece_image)
                file_no += 1
        rank_no += 1
        file_no = 0


def draw_diagram_for_fen(fen):
    """
    Creates an image for the given FEN position.
    :param fen: game position in FEN notation as string
    """

    image = create_image(BOARD_SIZE, BOARD_SIZE, color=BOARD_COLOR_BORDER)

    draw_board(image, SQUARE_SIZE, BORDER_SIZE, BORDER_SIZE, light=BOARD_COLOR_LIGHT, dark=BOARD_COLOR_DARK)
    draw_key(image, SQUARE_SIZE, BORDER_SIZE, BORDER_SIZE)

    fen_groups = fen.split(" ")
    pieces = fen_groups[0]
    draw_pieces(image, SQUARE_SIZE, BORDER_SIZE, BORDER_SIZE, pieces)

    return image
