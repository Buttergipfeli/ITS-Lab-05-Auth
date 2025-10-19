document.addEventListener("DOMContentLoaded", () => {
    const authToken = localStorage.getItem("authToken")
    if (authToken) {
        window.location.href = "/dashboard"
    } else {
        window.location.href = "/login"
    }
})
