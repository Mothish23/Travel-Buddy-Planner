const API_BASE = "/api";

function getToken() {
    return localStorage.getItem("token");
}

function getRole() {
    return localStorage.getItem("role");
}

function isLoggedIn() {
    return !!getToken();
}

function logout() {

    localStorage.removeItem("token");
    localStorage.removeItem("name");
    localStorage.removeItem("email");
    localStorage.removeItem("role");

    window.location.href = "index.html";
}

async function apiFetch(url, options = {}) {

    const headers = {
        ...(options.headers || {})
    };

    if (!(options.body instanceof FormData)) {
        headers["Content-Type"] = "application/json";
    }

    const token = getToken();

    if (token) {
        headers["Authorization"] = "Bearer " + token;
    }

    const response = await fetch(API_BASE + url, {
        ...options, headers: headers
    });

    if (response.status === 401) {
        logout();
        throw new Error("Session expired. Please login again.");
    }

    if (!response.ok) {
        let message = "Something went wrong";

        try {

            const data = await response.json();

            if (data.message) {
                message = data.message;
            } else {

                const firstError = Object.values(data)[0];

                if (firstError) {
                    message = firstError;
                }
            }

        } catch (error) {
            // Ignore JSON parsing error
        }

        throw new Error(message);
    }

    if (response.status === 204) {
        return null;
    }

    return response.json();
}