import { SERVER_BASE_URL } from "../constants.js"

const LOGIN_URL = `${SERVER_BASE_URL}/auth/login`

document.addEventListener("DOMContentLoaded", () => {
    const loginBtn = document.getElementById("login-button")

    loginBtn.addEventListener("click", async () => {
        const email = document.getElementById("email-input").value.trim()
        const password = document.getElementById("password-input").value.trim()

        if (!email || !password) {
            alert("Please enter both email and password.")
            return
        }

        try {
            const response = await fetch(LOGIN_URL, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ email, password })
            })

            if (!response.ok) {
                throw new Error("Failed to login. Please check your credentials.")
            }

            const data = await response.json()
            localStorage.setItem("authToken", data.token)

            window.location.href = "/dashboard"
        } catch (error) {
            console.error(error)
            alert("Login error: " + error.message)
        }
    })

    const registerBtn = document.getElementById("register-button")
    registerBtn.addEventListener("click", async () => {
        window.location.href = "/register"
    })
})
