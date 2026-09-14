import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import api from "../api/axiosConfig";

export const fetchMyProjects = createAsyncThunk(
  "projects/fetchMyProjects",
  async () => {
    const response = await api.get("/my/projects");
    return response.data.content;
  },
);

export const createProject = createAsyncThunk(
  "projects/createProject",
  async (projectData) => {
    const response = await api.post("/projects", projectData);
    return response.data;
  },
);

const projectsSlice = createSlice({
  name: "projects",
  initialState: {
    items: [],
    status: "idle",
    error: null,
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchMyProjects.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchMyProjects.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.items = action.payload;
      })
      .addCase(fetchMyProjects.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.error.message;
      })
      .addCase(createProject.fulfilled, (state, action) => {
        state.items.push(action.payload);
      });
  },
});

export default projectsSlice.reducer;
