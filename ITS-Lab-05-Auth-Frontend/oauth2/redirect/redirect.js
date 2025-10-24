document.addEventListener("DOMContentLoaded", () => {
    const params = new URLSearchParams(location.hash.slice(1));
    const token = params.get("token");
    if (token) {
        localStorage.setItem("authToken", token);
        window.location.href = `${location.origin}/dashboard`
    } else {
        alert("OAuth-Error, no token received.");
        window.location.href = `${location.origin}/login`
    }
})
