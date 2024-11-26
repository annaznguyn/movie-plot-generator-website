import React, { useState } from 'react';
import axios from 'axios';

interface CharacterEditDrawerProps {
  character?: Character;
  storyId: number;
  onSave: (character: Character) => void;
  onCancel: () => void;
}

interface Character {
  id?: number;
  firstName: string;
  lastName: string;
  context: string;
  background: string;
}

const CharacterEditDrawer: React.FC<CharacterEditDrawerProps> = ({ character, storyId, onSave, onCancel }) => {
  const [firstName, setFirstName] = useState(character?.firstName || '');
  const [lastName, setLastName] = useState(character?.lastName || '');
  const [context, setContext] = useState(character?.context || '');
  const [generatedBackground, setGeneratedBackground] = useState(character?.background || '');

  const handleGenerate = async () => {
    try {
      const response = await axios.get(`/stories/${storyId}/characters/generation`, {
        data: { firstName, lastName, context }
      });
      setGeneratedBackground(response.data);
    } catch (error) {
      console.error('Error generating background:', error);
    }
  };

  const handleSave = () => {
    onSave({
      id: character?.id,
      firstName,
      lastName,
      context,
      background: generatedBackground
    });
  };

  return (
    <div className="p-4 w-full max-w-md">
      <div className="mb-4">
        <label className="block text-sm font-medium text-gray-700">First Name</label>
        <input
          type="text"
          value={firstName}
          onChange={(e) => setFirstName(e.target.value)}
          className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
        />
      </div>
      <div className="mb-4">
        <label className="block text-sm font-medium text-gray-700">Last Name</label>
        <input
          type="text"
          value={lastName}
          onChange={(e) => setLastName(e.target.value)}
          className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
        />
      </div>
      <div className="mb-4">
        <label className="block text-sm font-medium text-gray-700">Context</label>
        <textarea
          value={context}
          onChange={(e) => setContext(e.target.value)}
          className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
          rows={3}
        />
      </div>
      <button
        onClick={handleGenerate}
        className="w-full mb-4 py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
      >
        Generate
      </button>
      <div className="mb-4">
        <label className="block text-sm font-medium text-gray-700">Generated Background</label>
        <textarea
          value={generatedBackground}
          readOnly
          className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
          rows={5}
        />
      </div>
      <div className="flex justify-between">
        <button
          onClick={onCancel}
          className="py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
        >
          Cancel
        </button>
        <button
          onClick={handleSave}
          className="py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
        >
          Save
        </button>
      </div>
    </div>
  );
};

export default CharacterEditDrawer;
