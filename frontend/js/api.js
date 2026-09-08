// Small helper wrapper around fetch() that attaches the JWT and handles JSON.

function getToken() {
  return localStorage.getItem("token");
}

function setSession(token, name, role) {
  localStorage.setItem("token", token);
  localStorage.setItem("name", name);
  localStorage.setItem("role", role);
}

function clearSession() {
  localStorage.removeItem("token");
  localStorage.removeItem("name");
  localStorage.removeItem("role");
}

function requireAuth() {
  if (!getToken()) {
    window.location.href = "login.html";
  }
}

async function apiRequest(path, method = "GET", body = null) {
  const headers = { "Content-Type": "application/json" };
  const token = getToken();
  if (token) headers["Authorization"] = "Bearer " + token;

  const response = await fetch(API_BASE_URL + path, {
    method,
    headers,
    body: body ? JSON.stringify(body) : null,
  });

  const data = await response.json().catch(() => ({}));

  if (!response.ok) {
    throw new Error(data.error || "Request failed");
  }
  return data;
}
