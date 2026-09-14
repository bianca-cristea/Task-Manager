import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import api from "../api/axiosConfig";

export const fetchMyTasks = createAsyncThunk("tasks/fetchMyTasks", async () => {
  const response = await api.get("/my/tasks");
  return response.data.content;
});

export const createTask = createAsyncThunk(
  "tasks/createTask",
  async (taskData) => {
    const response = await api.post("/tasks", taskData);
    return response.data;
  },
);

export const updateTaskStatus = createAsyncThunk(
  "tasks/updateTaskStatus",
  async ({ id, taskData }) => {
    const response = await api.put(`/tasks/${id}`, taskData);
    return response.data;
  },
);

const tasksSlice = createSlice({
  name: "tasks",
  initialState: {
    items: [],
    status: "idle",
    error: null,
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchMyTasks.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchMyTasks.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.items = action.payload;
      })
      .addCase(fetchMyTasks.rejected, (state) => {
        state.status = "failed";
      })
      .addCase(createTask.fulfilled, (state, action) => {
        state.items.push(action.payload);
      })
      .addCase(updateTaskStatus.fulfilled, (state, action) => {
        const index = state.items.findIndex(
          (t) => t.taskId === action.payload.taskId,
        );
        if (index !== -1) state.items[index] = action.payload;
      });
  },
});

export default tasksSlice.reducer;
