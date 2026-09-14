import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { fetchMyProjects, createProject } from "../redux/projectsSlice";
import { logout } from "../redux/authSlice";

function ProjectsPage() {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const { items: projects, status } = useSelector((state) => state.projects);
  const { user } = useSelector((state) => state.auth);

  const [showForm, setShowForm] = useState(false);
  const [projectName, setProjectName] = useState("");
  const [description, setDescription] = useState("");

  useEffect(() => {
    dispatch(fetchMyProjects());
  }, [dispatch]);

  const handleCreate = async (e) => {
    e.preventDefault();
    await dispatch(createProject({ projectName, description }));
    setProjectName("");
    setDescription("");
    setShowForm(false);
  };

  const handleLogout = () => {
    dispatch(logout());
    navigate("/login");
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <header className="bg-white shadow-sm">
        <div className="max-w-4xl mx-auto px-4 py-4 flex justify-between items-center">
          <h1 className="text-xl font-semibold text-gray-900">My Projects</h1>
          <div className="flex items-center gap-4">
            <span className="text-sm text-gray-600">Hi, {user?.username}</span>
            <button
              onClick={handleLogout}
              className="text-sm text-red-600 hover:text-red-700 font-medium"
            >
              Logout
            </button>
          </div>
        </div>
      </header>

      <main className="max-w-4xl mx-auto px-4 py-8">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-lg font-medium text-gray-700">
            {projects.length} project{projects.length !== 1 ? "s" : ""}
          </h2>
          <button
            onClick={() => setShowForm(!showForm)}
            className="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-lg text-sm font-medium"
          >
            {showForm ? "Cancel" : "+ New Project"}
          </button>
        </div>

        {showForm && (
          <form
            onSubmit={handleCreate}
            className="bg-white rounded-xl shadow-sm p-6 mb-6 space-y-4"
          >
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Project Name
              </label>
              <input
                type="text"
                value={projectName}
                onChange={(e) => setProjectName(e.target.value)}
                required
                className="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Description
              </label>
              <textarea
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                required
                rows={3}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
              />
            </div>
            <button
              type="submit"
              className="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-lg text-sm font-medium"
            >
              Create Project
            </button>
          </form>
        )}

        {status === "loading" && (
          <p className="text-gray-500">Loading projects...</p>
        )}

        {status === "failed" && (
          <p className="text-gray-500">
            No projects yet. Create your first one!
          </p>
        )}

        {status === "succeeded" && projects.length === 0 && (
          <p className="text-gray-500">
            No projects yet. Create your first one!
          </p>
        )}

        <div className="grid gap-4 sm:grid-cols-2">
          {projects.map((project) => (
            <div
              key={project.projectId}
              onClick={() => navigate(`/projects/${project.projectId}/tasks`)}
              className="bg-white rounded-xl shadow-sm p-5 cursor-pointer hover:shadow-md transition-shadow"
            >
              <h3 className="font-medium text-gray-900 mb-1">
                {project.projectName}
              </h3>
              <p className="text-sm text-gray-600">{project.description}</p>
            </div>
          ))}
        </div>
      </main>
    </div>
  );
}

export default ProjectsPage;
