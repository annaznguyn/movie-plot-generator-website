import React, { useState, useEffect } from "react";

interface Node {
  id: number;
  x: number;
  y: number;
  width: number;
  height: number;
  color: string;
  text: string;
  description?: string;
  genre?: string;
  characters?: string;
  context?: string;
}

interface NodeFormData {
  name: string;
  description: string;
  genre: string;
  characters: string;
  context: string;
}

interface NodeDrawerProps {
  node: Node | null;
  onUpdate: (formData: NodeFormData) => void;
  toggleNodeDrawer: () => void;
  nodes: Node[];
  onGenerate: () => void;
}

const NodeDrawer: React.FC<NodeDrawerProps> = ({ node, onUpdate, toggleNodeDrawer, nodes, onGenerate }) => {
  const [formData, setFormData] = useState<NodeFormData>({
    name: "",
    description: "",
    genre: "",
    characters: "",
    context: ""
  });

  useEffect(() => {
    if (node) {
      setFormData({
        name: node.text || "",
        description: node.description || "",
        genre: node.genre || "",
        characters: node.characters || "",
        context: node.context || ""
      });
    }
  }, [node]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onUpdate(formData);
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  return (
    <div className="h-full bg-white border-r-2 border-slate-700">
      <div className="h-full sm:w-[200px] lg:w-[400px] border-2 rounded-md">
        <div className="flex justify-end p-2">
          <button 
            onClick={toggleNodeDrawer}
            className="p-1 hover:bg-gray-100 rounded-full transition-colors"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              strokeWidth={1.5}
              stroke="currentColor"
              className="w-6 h-6"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                d="M6 18 18 6M6 6l12 12"
              />
            </svg>
          </button>
        </div>
        
        <form onSubmit={handleSubmit} className="space-y-4 px-6">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Name
            </label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              placeholder="Name"
              className="w-full px-3 py-2 border bg-white border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Description
            </label>
            <input
              type="text"
              name="description"
              value={formData.description}
              onChange={handleChange}
              placeholder="Description"
              className="w-full px-3 py-2 border bg-white border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Genre
            </label>
            <input
              type="text"
              name="genre"
              value={formData.genre}
              onChange={handleChange}
              placeholder="Genre"
              className="w-full px-3 py-2 border bg-white border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Characters
            </label>
            <input
              type="text"
              name="characters"
              value={formData.characters}
              onChange={handleChange}
              placeholder="Separate character names with ' , '"
              className="w-full px-3 py-2 border bg-white border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Context
            </label>
            <textarea
              name="context"
              value={formData.context}
              onChange={handleChange}
              className="w-full h-40 px-3 py-2 border bg-white border-gray-300 rounded-md resize-y focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Write some context"
            />
          </div>

          <div className="pt-4 pb-2">
            <button
              type="button"
              onClick={onGenerate}
              className="w-full px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
            >
              Generate
            </button>
          </div>

          <div className="pt-2 pb-6 flex justify-between">
            <button
              type="button"
              onClick={toggleNodeDrawer}
              className="px-4 py-2 bg-red-500 text-white rounded-md hover:bg-red-600 transition-colors"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition-colors"
            >
              Save
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default NodeDrawer;
