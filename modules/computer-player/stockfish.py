import subprocess
import logging


def calculate_move(fen, stockfish_executable ='stockfish'):
    log = logging.getLogger(__name__)

    p = subprocess.Popen([stockfish_executable],
                        shell=True,
                        stdin=subprocess.PIPE,
                        stdout=subprocess.PIPE,
                        )

    cmd = 'position fen ' + fen + '\n'
    p.stdin.write(cmd.encode())
    p.stdin.write('go\n'.encode())
    p.stdin.flush()
    best_move = ""
    while best_move == "":
        line = p.stdout.readline()
        line = str(line, encoding='utf-8')
        log.debug(line)
        if line.startswith("bestmove"):
            best_move = line.split()[1]

    p.stdin.write('quit\n'.encode())
    p.stdin.flush()

    return best_move

