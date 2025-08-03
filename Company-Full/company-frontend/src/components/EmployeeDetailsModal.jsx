import React from "react";
import { XMarkIcon, ClipboardDocumentListIcon } from "@heroicons/react/24/solid";

function EmployeeDetailsModal({ projects, onClose }) {
  return (
    <div className="fixed inset-0 bg-black/50 backdrop-blur-sm flex justify-center items-center z-50">
      <div className="bg-white/10 backdrop-blur-lg rounded-2xl shadow-xl p-6 w-full max-w-lg border border-white/20 animate-scaleIn">
        
        {/* Header */}
        <div className="flex justify-between items-center mb-4">
          <h2 className="flex items-center gap-2 text-xl font-bold text-white">
            <ClipboardDocumentListIcon className="w-6 h-6 text-blue-300" />
            Employee Projects
          </h2>
          <button onClick={onClose} className="text-white hover:text-red-300 transition">
            <XMarkIcon className="w-6 h-6" />
          </button>
        </div>

        {/* Content */}
        {projects.length === 0 ? (
          <p className="text-gray-300">No projects found for this employee.</p>
        ) : (
          <ul className="space-y-3">
            {projects.map((proj) => (
              <li key={proj.projectId} className="bg-white/10 p-3 rounded-lg border border-white/10">
                <strong className="text-white">{proj.projectName}</strong>
                <p className="text-gray-300 text-sm">{proj.durationText}</p>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}

export default EmployeeDetailsModal;
