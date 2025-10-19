import { SERVER_BASE_URL } from "../constants.js"

const USER_URL = `${SERVER_BASE_URL}/user`

const updateUserInfoDisplay = (userInfo) => {
    document.getElementById("email-output").textContent = userInfo.email
    document.getElementById("nickname-output").textContent = userInfo.nickname
}

const checkAuth = () => {
    const authToken = localStorage.getItem("authToken")
    if (!authToken) {
        window.location.href = "/login"
    }

    return authToken
}

const loadUserInfo = async (token) => {
    try {
        const response = await fetch(USER_URL, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            }
        })

        if (!response.ok) {
            alert("Failed to load user info. Please log in again.")
            window.location.href = "/login"
            return
        }

        const userInfo = await response.json()
        updateUserInfoDisplay(userInfo)
    } catch (error) {
        console.error("Error loading user info:", error)
    }
}

document.addEventListener("DOMContentLoaded", () => {
    loadUserInfo(checkAuth())

    const changeNicknameBtn = document.getElementById("change-nickname-button")
    changeNicknameBtn.addEventListener("click", async () => {
        const token = checkAuth()

        const newNickname = document.getElementById("new-nickname-input").value.trim()
        if (!newNickname) {
            alert("Please enter a new nickname.")
            return
        }

        try {
            const response = await fetch(USER_URL, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify({ nickname: newNickname })
            })

            if (!response.ok) {
                throw new Error("Failed to update nickname.")
            }

            const updatedUserInfo = await response.json()
            updateUserInfoDisplay(updatedUserInfo)
        } catch (error) {
            console.error("Error updating nickname:", error)
        }
    })

    const logoutBtn = document.getElementById("log-out-button")
    logoutBtn.addEventListener("click", () => {
        localStorage.removeItem("authToken")
        window.location.href = "/login"
    })
})
