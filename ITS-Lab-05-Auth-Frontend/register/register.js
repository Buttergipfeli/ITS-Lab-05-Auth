import { SERVER_BASE_URL } from "../constants.js"

const REGISTER_URL = `${SERVER_BASE_URL}/auth/register`
const REGISTER_ERROR_MESSAGE = "Failed to register. Please check your details."

document.addEventListener("DOMContentLoaded", () => {
    const registerBtn = document.getElementById("register-button")

    registerBtn.addEventListener("click", async () => {
        const email = document.getElementById("email-input").value.trim()
        const nickname = document.getElementById("nickname-input").value.trim()
        const password = document.getElementById("password-input").value.trim()

        if (!email || !nickname || !password) {
            alert("Please enter all fields.")
            return
        }

        try {
            const response = await fetch(REGISTER_URL, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ email, nickname, password })
            })

            if (!response.ok) {
                throw new Error(REGISTER_ERROR_MESSAGE)
            }

            const data = await response.json()
            localStorage.setItem("authToken", data.token)

            window.location.href = "/dashboard"
        } catch (error) {
            console.error(error)
            alert("Registration error: " + REGISTER_ERROR_MESSAGE)
        }
    })

    const loginBtn = document.getElementById("login-button")
    loginBtn.addEventListener("click", async () => {
        window.location.href = "/login"
    })
})
