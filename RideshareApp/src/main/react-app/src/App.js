import logo from "./logo.svg";
import "./App.css";
import { NavLink, useNavigate, Outlet } from "react-router-dom";

function App() {
  const navigate = useNavigate();
  function handleLogout(e) {
    localStorage.removeItem("USER");
    navigate("/home");
  }

  return (
    <div className="App">
      <nav className="navMenu">
        {!localStorage.getItem("USER") && <NavLink to="/login">Login</NavLink>}
        {!localStorage.getItem("USER") && (
          <NavLink to="/register">Register</NavLink>
        )}
        {localStorage.getItem("USER") && (
          <NavLink>
            <button onClick={(e) => handleLogout(e)}>Sign out</button>
          </NavLink>
        )}

        <NavLink to="/home">Home</NavLink>
        {localStorage.getItem("USER") && <NavLink to="/Profile">Profile</NavLink>}
      </nav>
      <Outlet />
    </div>
  );
}

export default App;
