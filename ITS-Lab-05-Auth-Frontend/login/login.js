import { SERVER_BASE_URL } from "../constants.js"

const LOGIN_URL = `${SERVER_BASE_URL}/auth/login`
const SOCIAL_LOGIN_URL = `${SERVER_BASE_URL}/login/oauth2/code/google`
const LOGIN_ERROR_MESSAGE = "Failed to login. Please check your credentials."

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
                throw new Error(LOGIN_ERROR_MESSAGE)
            }

            const data = await response.json()
            localStorage.setItem("authToken", data.token)

            window.location.href = "/dashboard"
        } catch (error) {
            console.error(error)
            alert("Login error: " + LOGIN_ERROR_MESSAGE)
        }
    })

    const registerBtn = document.getElementById("register-button")
    registerBtn.addEventListener("click", async () => {
        window.location.href = "/register"
    })

    const socialLoginBtn = document.getElementById("social-login-google-button")
    socialLoginBtn.addEventListener("click", async () => {
        window.location.href = SOCIAL_LOGIN_URL
    })
})
