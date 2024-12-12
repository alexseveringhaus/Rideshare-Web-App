import React from "react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../output.css";
import "../index.css";
export default function Profile() {
  // Load profile from localStorage or set default values
  const [profile, setProfile] = useState(() => {
    const savedProfile = localStorage.getItem("profile");
    return savedProfile
      ? JSON.parse(savedProfile)
      : {
          firstName: "",
          lastName: "",
          age: "",
          education: "Undergraduate",
          gender: "",
        }; // Default placeholders
  });

  const [editable, setEditable] = useState(null);

  const handleChange = (field, value) => {
    const updatedProfile = { ...profile, [field]: value };
    setProfile(updatedProfile);
    localStorage.setItem("profile", JSON.stringify(updatedProfile)); // Save to localStorage
  };

  const handleEdit = (field) => {
    setEditable(field);
  };

  const handleSave = () => {
    alert("Profile saved successfully!\n" + JSON.stringify(profile, null, 2));
    setEditable(null);
  };

  return (
    <div className="flex justify-center items-center min-h-screen bg-gray-100">
      <div className="profile-container bg-white shadow-md rounded-lg p-8 w-full max-w-3xl">
        <h1 className="text-5xl font-bold text-center text-red-800 mb-4">
          User Profile
        </h1>

        {/* First Name Field */}
        <div className="form-group space-y-4">
          <label className="block text-black font-bold mb-1">First Name:</label>
          {editable === "firstName" ? (
            <input
              type="text"
              value={profile.firstName}
              onChange={(e) => handleChange("firstName", e.target.value)}
              placeholder="Enter your first name"
              className="w-full px-4 py-2 mt-1 border border-gray-300 rounded-md shadow-sm"
            />
          ) : (
            <div
              onClick={() => handleEdit("firstName")}
              className="cursor-pointer bg-gray-100 px-4 py-2 mt-1 border border-gray-300 rounded-md shadow-sm"
            >
              {profile.firstName || "Enter your first name"}
            </div>
          )}
        </div>

        {/* Add space between First Name and Last Name */}
        <div className="h-4"></div>

        {/* Last Name Field */}
        <div className="form-group space-y-4">
          <label className="block text-black font-bold mb-1">Last Name:</label>
          {editable === "lastName" ? (
            <input
              type="text"
              value={profile.lastName}
              onChange={(e) => handleChange("lastName", e.target.value)}
              placeholder="Enter your last name"
              className="w-full px-4 py-2 mt-1 border border-gray-300 rounded-md shadow-sm"
            />
          ) : (
            <div
              onClick={() => handleEdit("lastName")}
              className="cursor-pointer bg-gray-100 px-4 py-2 mt-1 border border-gray-300 rounded-md shadow-sm"
            >
              {profile.lastName || "Enter your last name"}
            </div>
          )}
        </div>

        {/* Other Fields */}
        {["age", "education", "gender"].map((key) => (
          <div className="form-group space-y-4" key={key}>
            <label className="block text-black font-bold mb-1">
              {key.charAt(0).toUpperCase() + key.slice(1)}:
            </label>
            {editable === key ? (
              key === "education" ? (
                <select
                  value={profile[key]}
                  onChange={(e) => handleChange(key, e.target.value)}
                  className="w-full px-4 py-2 mt-1 border border-gray-300 rounded-md shadow-sm"
                >
                  <option value="Undergraduate">Undergraduate</option>
                  <option value="Masters">Masters</option>
                  <option value="PHD">PHD</option>
                </select>
              ) : (
                <input
                  type={key === "age" ? "number" : "text"}
                  value={profile[key]}
                  onChange={(e) => handleChange(key, e.target.value)}
                  placeholder={`Enter your ${key}`}
                  className="w-full px-4 py-2 mt-1 border border-gray-300 rounded-md shadow-sm"
                />
              )
            ) : (
              <div
                onClick={() => handleEdit(key)}
                className="cursor-pointer bg-gray-100 px-4 py-2 mt-1 border border-gray-300 rounded-md shadow-sm"
              >
                {profile[key] || `Enter your ${key}`}
              </div>
            )}
          </div>
        ))}

        <button
          onClick={handleSave}
          className="w-full bg-yellow-600 text-white py-2 px-4 rounded-md focus:outline-none focus:ring-2 focus:ring-offset-2 hover:bg-yellow-700 mt-4"
        >
          Save
        </button>
      </div>
    </div>
  );
}
