import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useParams, useNavigate } from "react-router-dom";
import {
  fetchMyTasks,
  createTask,
  updateTaskStatus,
} from "../redux/tasksSlice";

function TasksPage() {
  const { projectId } = useParams();
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const { items: allTasks, status } = useSelector((state) => state.tasks);

  const tasks = allTasks.filter((task) => task.projectId === Number(projectId));

  const [showForm, setShowForm] = useState(false);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [categoryId, setCategoryId] = useState("");

  useEffect(() => {
    dispatch(fetchMyTasks());
  }, [dispatch]);

  const handleCreate = async (e) => {
    e.preventDefault();
    await dispatch(
      createTask({
        title,
        description,
        status: "TO_DO",
        projectId: Number(projectId),
        categoryId: Number(categoryId),
      }),
    );
    setTitle("");
    setDescription("");
    setCategoryId("");
    setShowForm(false);
  };

  const handleStatusChange = (task, newStatus) => {
    dispatch(
      updateTaskStatus({
        id: task.taskId,
        taskData: { ...task, status: newStatus },
      }),
    );
  };

  const statusColors = {
    TO_DO: "bg-gray-100 text-gray-700",
    IN_PROGRESS: "bg-yellow-100 text-yellow-700",
    DONE: "bg-green-100 text-green-700",
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <header className="bg-white shadow-sm">
        <div className="max-w-4xl mx-auto px-4 py-4 flex justify-between items-center">
          <button
            onClick={() => navigate("/projects")}
            className="text-sm text-indigo-600 hover:underline"
          >
            ← Back to Projects
          </button>
          <h1 className="text-xl font-semibold text-gray-900">Tasks</h1>
        </div>
      </header>

      <main className="max-w-4xl mx-auto px-4 py-8">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-lg font-medium text-gray-700">
            {tasks.length} task{tasks.length !== 1 ? "s" : ""}
          </h2>
          <button
            onClick={() => setShowForm(!showForm)}
            className="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-lg text-sm font-medium"
          >
            {showForm ? "Cancel" : "+ New Task"}
          </button>
        </div>

        {showForm && (
          <form
            onSubmit={handleCreate}
            className="bg-white rounded-xl shadow-sm p-6 mb-6 space-y-4"
          >
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Title
              </label>
              <input
                type="text"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
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
                rows={2}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Category ID
              </label>
              <input
                type="number"
                value={categoryId}
                onChange={(e) => setCategoryId(e.target.value)}
                required
                className="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
              />
            </div>
            <button
              type="submit"
              className="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-lg text-sm font-medium"
            >
              Create Task
            </button>
          </form>
        )}

        {status === "loading" && (
          <p className="text-gray-500">Loading tasks...</p>
        )}

        <div className="space-y-3">
          {tasks.map((task) => (
            <div
              key={task.taskId}
              className="bg-white rounded-xl shadow-sm p-5 flex justify-between items-center"
            >
              <div>
                <h3 className="font-medium text-gray-900">{task.title}</h3>
                <p className="text-sm text-gray-600">{task.description}</p>
              </div>
              <select
                value={task.status}
                onChange={(e) => handleStatusChange(task, e.target.value)}
                className={`text-xs font-medium px-3 py-1 rounded-full border-0 ${statusColors[task.status]}`}
              >
                <option value="TO_DO">To Do</option>
                <option value="IN_PROGRESS">In Progress</option>
                <option value="DONE">Done</option>
              </select>
            </div>
          ))}
        </div>
      </main>
    </div>
  );
}

export default TasksPage;
