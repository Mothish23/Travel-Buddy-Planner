const loginForm = document.getElementById("loginForm");

const registerForm = document.getElementById("registerForm");


if (loginForm) {

    loginForm.addEventListener("submit", async function (event) {

        event.preventDefault();

        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        try {
            const data = await apiFetch("/auth/login", {
                method: "POST",
                body: JSON.stringify({
                    email, password
                })
            });

            localStorage.setItem("token", data.token);
            localStorage.setItem("name", data.name);
            localStorage.setItem("email", data.email);
            localStorage.setItem("role", data.role);
            window.location.href = "dashboard.html";

        } catch (error) {
            document.getElementById("message").textContent = error.message;
        }
    });
}


if (registerForm) {
    registerForm.addEventListener("submit", async function (event) {
        event.preventDefault();
        const name = document.getElementById("name").value;
        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        try {
            await apiFetch("/auth/register", {
                method: "POST",
                body: JSON.stringify({
                    name, email, password
                })
            });

            alert("Registration successful!");

            window.location.href = "login.html";

        } catch (error) {
            document.getElementById("message").textContent = error.message;
        }
    });
}