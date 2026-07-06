/**
 * 登录态工具：token / 身份读取与同步
 */
function getUserToken() {
    return localStorage.getItem("user_token");
}

function decodeJwtPayload(token) {
    if (!token) {
        return null;
    }
    try {
        var base64 = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
        var pad = base64.length % 4;
        if (pad) {
            base64 += new Array(5 - pad).join('=');
        }
        return JSON.parse(atob(base64));
    } catch (e) {
        return null;
    }
}

function getIdentityFromToken() {
    var payload = decodeJwtPayload(getUserToken());
    return payload && payload.identity ? payload.identity : null;
}

/** 若 localStorage 缺少 user_identity，则从 token 补写；始终以 token 为准 */
function syncUserIdentity() {
    var fromToken = getIdentityFromToken();
    if (fromToken) {
        var normalized = fromToken.toUpperCase();
        localStorage.setItem("user_identity", normalized);
        return normalized;
    }
    var stored = localStorage.getItem("user_identity");
    if (stored && stored !== 'undefined' && stored !== 'null') {
        return stored;
    }
    return null;
}

function isAdmin() {
    var identity = syncUserIdentity();
    return identity && identity.toUpperCase() === 'ADMIN';
}

function authHeaders() {
    var token = getUserToken();
    return token ? { 'user_token': token } : {};
}

function ensureLoggedIn(loginUrl) {
    loginUrl = loginUrl || '/blogin.html';
    if (!getUserToken()) {
        alert("请先登录管理员账号");
        location.href = loginUrl;
        return false;
    }
    syncUserIdentity();
    return true;
}
