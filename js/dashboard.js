if (!isLoggedIn()) {
    window.location.href = "login.html";
}

const welcomeName = document.getElementById("welcomeName");

if (welcomeName) {
    welcomeName.textContent = localStorage.getItem("name") || "";
}

async function loadTrips() {
    try {
        const trips = await apiFetch("/trips");
        const container = document.getElementById("tripContainer");

        container.innerHTML = "";

        if (!trips || trips.length === 0) {
            container.innerHTML = `
                <div class="empty-state">
                    <h3>No trips yet</h3>
                    <p>Create your first adventure.</p>
                    <a href="create-trip.html" class="btn">
                        Create Trip
                    </a>
                </div>
            `;
            return;
        }

        trips.forEach(trip => {
            const card = document.createElement("div");
            card.className = "trip-card";

            const imageUrl = trip.imageUrl || "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=800&q=80";

            card.innerHTML = `
                <img
                    src="${escapeHtml(imageUrl)}"
                    alt="${escapeHtml(trip.destination || "Trip")}"
                    onerror="this.onerror=null; this.src='https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=800&q=80';"
                >

                <div class="trip-card-content">

                    <span class="trip-location">
                        📍 ${escapeHtml(trip.destination || "")}
                    </span>

                    <h3>${escapeHtml(trip.tripName || "")}</h3>

                    <p>
                        ${escapeHtml(trip.description || "No description")}
                    </p>

                    <div class="trip-meta">
                        <span>${escapeHtml(trip.startDate || "")}</span>
                        <span>₹${escapeHtml(trip.budget ?? 0)}</span>
                    </div>

                    <div class="card-actions">

                        <a
                            class="btn small"
                            href="trip-details.html?id=${trip.id}">
                            View
                        </a>

                        <a
                            class="btn secondary small"
                            href="edit-trip.html?id=${trip.id}">
                            Edit
                        </a>

                        <button
                            class="btn danger small"
                            onclick="deleteTrip(${trip.id})">
                            Delete
                        </button>

                    </div>

                </div>
            `;

            container.appendChild(card);
        });

    } catch (error) {
        alert(error.message);
    }
}

async function deleteTrip(id) {
    if (!confirm("Are you sure you want to delete this trip?")) {
        return;
    }

    try {
        await apiFetch(`/trips/${id}`, {
            method: "DELETE"
        });

        await loadTrips();

    } catch (error) {
        alert(error.message);
    }
}

function escapeHtml(value) {
    return String(value ?? "")
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

loadTrips();
