import { useEffect, useState } from "react";
import { getProfile } from "../services/profileService";
import { useNavigate } from "react-router-dom";
import { UserCircleIcon } from "@heroicons/react/24/solid";

function Profile() {
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const data = await getProfile();
        setUser(data);
      } catch (error) {
        console.error("Failed to load profile:", error);
      }
    };
    fetchProfile();
  }, []);

  if (!user) {
    return <p className="text-center text-white mt-20 text-lg">Loading profile...</p>;
  }

  // 🔹 Tarih formatlama (gün.ay.yıl)
  const formatDate = (dateString) => {
    if (!dateString) return "—";
    const date = new Date(dateString);
    if (isNaN(date)) return "—";
    return date.toLocaleDateString("tr-TR"); // 22.07.2000
  };

  const profileFields = [
    { label: "Name", value: `${user.name} ${user.surname || ""}` },
    { label: "Email", value: user.email },
    { label: "Birthday", value: formatDate(user.birthday) }, // 🔹 formatlı tarih
    { label: "City", value: user.city || "—" },
    { label: "Department", value: user.department || "—" },
    { label: "Graduation Year", value: user.gradutaion_year || "—" },
    { label: "University", value: user.university || "—" },
    { label: "Job Start Date", value: formatDate(user.job_start_date) },
    { label: "Role", value: user.role },
    { label: "Status", value: user.status },
    { label: "Gender", value: user.gender || "—" },
    { label: "Title", value: user.title || "—" },
    { label: "TC", value: user.tc || "—" },
    { label: "Activated At", value: formatDate(user.activated_at) },
  ];

  return (
    <div className="min-h-screen flex flex-col bg-gradient-to-br from-purple-500 to-pink-500">
      {/* Navbar */}
      <nav className="bg-black bg-opacity-40 text-white px-6 py-4 flex justify-between items-center">
        <h1 className="font-bold text-lg">Company Portal</h1>
        <button
          onClick={() => navigate("/dashboard")}
          className="bg-blue-500 hover:bg-blue-600 px-4 py-2 rounded-md text-white"
        >
          ⬅ Back to Dashboard
        </button>
      </nav>

      {/* Profil Alanı */}
      <main className="flex flex-col items-center text-white p-8 w-full">
        {/* Avatar */}
        <UserCircleIcon className="w-24 h-24 text-white mb-4" />
        <h2 className="text-3xl font-bold mb-8">My Profile</h2>

        {/* Bilgiler */}
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-6 w-full px-4">
          {profileFields.map((field, index) => (
            <div
              key={index}
              className="bg-white bg-opacity-20 backdrop-blur-md p-4 rounded-lg shadow-md w-full"
            >
              <strong className="block text-gray-200">{field.label}:</strong>
              <span className="text-white">{field.value}</span>
            </div>
          ))}
        </div>
      </main>
    </div>
  );
}

export default Profile;
