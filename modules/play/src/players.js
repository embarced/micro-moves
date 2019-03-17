const JWT_COOKIE_NAME='flexess_jwt';

// See https://sabe.io/classes/javascript/cookies#reading-cookies
function readCookie(name) {
    const key = name + "=";
    const cookies = document.cookie.split(';');
    for (let i = 0; i < cookies.length; i++) {
        let cookie = cookies[i];
        while (cookie.charAt(0) === ' ') {
            cookie = cookie.substring(1, cookie.length);
        }
        if (cookie.indexOf(key) === 0) {
            return cookie.substring(key.length, cookie.length);
        }
    }
    return null;
}

function getJWTfromCookie() {
    return readCookie(JWT_COOKIE_NAME);
}

function getPayloadFromJWT(jwt_token) {
    let payloadDecoded = '';
    if (jwt_token !== null) {
        const payloadEncoded = jwt_token.split('.')[1];
        payloadDecoded = atob(payloadEncoded);
    }
    return payloadDecoded;
}

function getUserFromJWT(jwt_token) {
    const payload = getPayloadFromJWT(jwt_token);
    let jsonObject = null;
    if (payload !== '') {
        jsonObject = JSON.parse(payload);
    }

    return jsonObject;
}