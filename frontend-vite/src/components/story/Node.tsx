import React from 'react';

interface NodeProps {
  position: { x: number; y: number };
  width: number;
  height: number;
  color: string;
  text: string;
  onClick: () => void;
}

const Node: React.FC<NodeProps> = ({ position, width, height, color, text, onClick }) => {
  return (
    <g onClick={onClick}>
      <rect
        x={position.x}
        y={position.y}
        width={width}
        height={height}
        fill={color}
        style={{ cursor: 'pointer' }}
      />
      <text
        x={position.x + width / 2}
        y={position.y + height / 2}
        fill="#FFFFFF"
        fontSize="16"
        textAnchor="middle"
        dominantBaseline="middle"
      >
        {text}
      </text>
    </g>
  );
};

export default Node;