import React, { useState, useRef, useEffect } from 'react';
import StorySidebar from '../story/StorySidebar';
import NodeDrawer from '../story/NodeDrawer';

interface Position {
  x: number;
  y: number;
}

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
  children: number[]; // Array of child node IDs
  parentId?: number;
}

interface NodeFormData {
  name: string;
  description: string;
  genre: string;
  characters: string;
  context: string;
}

// interface InfiniteCanvasProps {
//     backgroundColor?: string;
//     minZoom?: number;
//     maxZoom?: number;
//   }
  

const InfiniteCanvas: React.FC = () => {
    const [position, setPosition] = useState<Position>({ x: 0, y: 0 });
    const [zoom, setZoom] = useState<number>(1);
    const [isDraggingCanvas, setIsDraggingCanvas] = useState<boolean>(false);
    const [isDraggingNode, setIsDraggingNode] = useState<boolean>(false);
    const [dragStart, setDragStart] = useState<Position>({ x: 0, y: 0 });
    const [nodes, setNodes] = useState<Node[]>([]);
    const [activeNode, setActiveNode] = useState<Node | null>(null);
    const [isDrawerOpen, setIsDrawerOpen] = useState<boolean>(false);
    const [deleteNodeId, setDeleteNodeId] = useState<number | null>(null);
    const [showDeleteDialog, setShowDeleteDialog] = useState<boolean>(false);
    const canvasRef = useRef<HTMLCanvasElement>(null);
    const minZoom = 0.1;
    const maxZoom = 10;
    const [newNodeParent, setNewNodeParent] = useState<Node | null>(null);

    useEffect(() => {
        if (nodes.length === 0) {
            // Add an initial node if the canvas is empty
            const initialNode: Node = {
                id: Date.now(),
                x: 100,
                y: 100,
                width: 200,
                height: 80,
                color: '#2f3033',
                text: 'Initial Node',
                children: [],
            };
            setNodes([initialNode]);
        }
    }, []);

    const drawCanvas = () => {
        const canvas = canvasRef.current;
        if (!canvas) return;

        const ctx = canvas.getContext('2d');
        if (!ctx) return;

        const dpr = window.devicePixelRatio || 1;
        const width = canvas.clientWidth * dpr;
        const height = canvas.clientHeight * dpr;
        canvas.width = width;
        canvas.height = height;

        ctx.fillStyle = '#ffffff';
        ctx.fillRect(0, 0, width, height);

        ctx.save();
        ctx.translate(position.x * dpr, position.y * dpr);
        ctx.scale(zoom, zoom);

        nodes.forEach(node => {
            drawNodeConnections(ctx, node);
        });

        nodes.forEach(node => {
            drawNode(ctx, node);
        });

        ctx.restore();
    };

    useEffect(() => {
        drawCanvas();
    }, [position, zoom, nodes]);

    const drawNode = (ctx: CanvasRenderingContext2D, node: Node) => {
        const radius = 10; // Radius for rounded corners

        ctx.beginPath();
        ctx.moveTo(node.x + radius, node.y);
        ctx.lineTo(node.x + node.width - radius, node.y);
        ctx.quadraticCurveTo(node.x + node.width, node.y, node.x + node.width, node.y + radius);
        ctx.lineTo(node.x + node.width, node.y + node.height - radius);
        ctx.quadraticCurveTo(node.x + node.width, node.y + node.height, node.x + node.width - radius, node.y + node.height);
        ctx.lineTo(node.x + radius, node.y + node.height);
        ctx.quadraticCurveTo(node.x, node.y + node.height, node.x, node.y + node.height - radius);
        ctx.lineTo(node.x, node.y + radius);
        ctx.quadraticCurveTo(node.x, node.y, node.x + radius, node.y);
        ctx.closePath();

        ctx.fillStyle = node.color;
        ctx.fill();

        ctx.fillStyle = '#FFFFFF';
        ctx.font = '16px Arial';
        ctx.textAlign = 'center';
        ctx.textBaseline = 'middle';
        ctx.fillText(node.text, node.x + node.width / 2, node.y + node.height / 2);

        // Draw plus button
        const buttonSize = 24;
        const buttonX = node.x + node.width + 10;
        const buttonY = node.y + (node.height - buttonSize) / 2;
        
        ctx.fillStyle = '#4CAF50';
        ctx.beginPath();
        ctx.arc(buttonX + buttonSize / 2, buttonY + buttonSize / 2, buttonSize / 2, 0, 2 * Math.PI);
        ctx.fill();

        ctx.strokeStyle = '#FFFFFF';
        ctx.lineWidth = 2;
        ctx.beginPath();
        ctx.moveTo(buttonX + 8, buttonY + buttonSize / 2);
        ctx.lineTo(buttonX + buttonSize - 8, buttonY + buttonSize / 2);
        ctx.moveTo(buttonX + buttonSize / 2, buttonY + 8);
        ctx.lineTo(buttonX + buttonSize / 2, buttonY + buttonSize - 8);
        ctx.stroke();

        // Draw edit button
        const editButtonSize = 24;
        const editButtonX = node.x + (node.width - editButtonSize) / 2;
        const editButtonY = node.y + node.height + 10;
        
        ctx.fillStyle = '#3498db';
        ctx.beginPath();
        ctx.arc(editButtonX + editButtonSize / 2, editButtonY + editButtonSize / 2, editButtonSize / 2, 0, 2 * Math.PI);
        ctx.fill();

        // Draw edit icon (simplified version)
        ctx.strokeStyle = '#FFFFFF';
        ctx.lineWidth = 2;
        ctx.beginPath();
        ctx.moveTo(editButtonX + 8, editButtonY + editButtonSize - 8);
        ctx.lineTo(editButtonX + editButtonSize - 8, editButtonY + 8);
        ctx.moveTo(editButtonX + 8, editButtonY + editButtonSize - 8);
        ctx.lineTo(editButtonX + 14, editButtonY + editButtonSize - 14);
        ctx.stroke();
    };

    const drawNodeControls = (ctx: CanvasRenderingContext2D, node: Node) => {
        ctx.fillStyle = '#ff4444';
        ctx.beginPath();
        ctx.arc(node.x + node.width - 10, node.y + 10, 8, 0, Math.PI * 2);
        ctx.fill();

        ctx.strokeStyle = '#ffffff';
        ctx.lineWidth = 2;
        ctx.beginPath();
        ctx.moveTo(node.x + node.width - 14, node.y + 6);
        ctx.lineTo(node.x + node.width - 6, node.y + 14);
        ctx.moveTo(node.x + node.width - 6, node.y + 6);
        ctx.lineTo(node.x + node.width - 14, node.y + 14);
        ctx.stroke();
    };

    const handleCanvasClick = (e: React.MouseEvent<HTMLCanvasElement>) => {
        const rect = canvasRef.current?.getBoundingClientRect();
        if (!rect) return;

        const mouseX = (e.clientX - rect.left - position.x) / zoom;
        const mouseY = (e.clientY - rect.top - position.y) / zoom;

        for (const node of nodes) {
            const buttonSize = 24;
            const buttonX = node.x + node.width + 10;
            const buttonY = node.y + (node.height - buttonSize) / 2;

            const dx = mouseX - (buttonX + buttonSize / 2);
            const dy = mouseY - (buttonY + buttonSize / 2);
            const distance = Math.sqrt(dx * dx + dy * dy);

            if (distance <= buttonSize / 2) {
                handleNodeAdd(node);
                return;
            }

            // Check for edit button click
            const editButtonSize = 24;
            const editButtonX = node.x + (node.width - editButtonSize) / 2;
            const editButtonY = node.y + node.height + 10;

            const edx = mouseX - (editButtonX + editButtonSize / 2);
            const edy = mouseY - (editButtonY + editButtonSize / 2);
            const editDistance = Math.sqrt(edx * edx + edy * edy);

            if (editDistance <= editButtonSize / 2) {
                handleEditNode(node);
                return;
            }
        }

        // Existing logic for handling node clicks or canvas clicks
        // ...
    };

    const handleNodeAdd = (parentNode?: Node) => {
        setNewNodeParent(parentNode || null);
        setActiveNode(null);
        setIsDrawerOpen(true);
    };

    const handleNodeUpdate = (formData: NodeFormData) => {
        if (!activeNode) return;

        const updatedNode = {
        ...activeNode,
        text: formData.name,
        description: formData.description,
        genre: formData.genre,
        characters: formData.characters,
        context: formData.context
        };

        setNodes(prevNodes =>
        prevNodes.map(node => (node.id === activeNode.id ? updatedNode : node))
        );
        setIsDrawerOpen(false);
        setActiveNode(null);
    };

    const handleNodeDelete = () => {
        if (deleteNodeId === null) return;
        setNodes(prevNodes => prevNodes.filter(node => node.id !== deleteNodeId));
        setShowDeleteDialog(false);
        setDeleteNodeId(null);
    };

    const handleMouseDown = (e: React.MouseEvent<HTMLCanvasElement>) => {
        const rect = canvasRef.current?.getBoundingClientRect();
        if (!rect) return;

        const mouseX = e.clientX - rect.left;
        const mouseY = e.clientY - rect.top;
        const transformedX = (mouseX - position.x) / zoom;
        const transformedY = (mouseY - position.y) / zoom;

        const clickedNode = nodes.find(node => {
        const deleteX = node.x + node.width - 10;
        const deleteY = node.y + 10;
        const distance = Math.sqrt(
            Math.pow(transformedX - deleteX, 2) + Math.pow(transformedY - deleteY, 2)
        );
        return distance <= 8;
        });

        if (clickedNode) {
        setDeleteNodeId(clickedNode.id);
        setShowDeleteDialog(true);
        return;
        }

        const selectedNode = nodes.find(
        node =>
            transformedX >= node.x &&
            transformedX <= node.x + node.width &&
            transformedY >= node.y &&
            transformedY <= node.y + node.height
        );

        if (selectedNode) {
        setActiveNode(selectedNode);
        setIsDraggingNode(true);
        setDragStart({ x: transformedX - selectedNode.x, y: transformedY - selectedNode.y });
        setIsDrawerOpen(false); // Close the drawer when starting to drag a node
        return;
        }

        setIsDraggingCanvas(true);
        setDragStart({ x: mouseX, y: mouseY });
    };

    const handleMouseMove = (e: React.MouseEvent<HTMLCanvasElement>): void => {
        if (isDraggingNode && activeNode) {
            const rect = canvasRef.current?.getBoundingClientRect();
            const mouseX = e.clientX - (rect?.left || 0);
            const mouseY = e.clientY - (rect?.top || 0);

            const transformedX = (mouseX - position.x) / zoom;
            const transformedY = (mouseY - position.y) / zoom;

            setNodes(prevNodes => {
                const updatedNodes = prevNodes.map(node => {
                    if (node.id === activeNode.id) {
                        return {
                            ...node,
                            x: transformedX - dragStart.x,
                            y: transformedY - dragStart.y
                        };
                    }
                    // Move child nodes along with the parent
                    if (node.parentId === activeNode.id) {
                        return {
                            ...node,
                            x: transformedX - dragStart.x + 300,
                            y: transformedY - dragStart.y
                        };
                    }
                    return node;
                });
                return updatedNodes;
            });
        } else if (isDraggingCanvas) {
            const rect = canvasRef.current?.getBoundingClientRect();
            const mouseX = e.clientX - (rect?.left || 0);
            const mouseY = e.clientY - (rect?.top || 0);

            // Calculate the difference in mouse movement
            const dx = mouseX - dragStart.x;
            const dy = mouseY - dragStart.y;

            // Update the canvas position
            setPosition(prev => ({
                x: prev.x + dx,
                y: prev.y + dy
            }));

            // Update drag start position for the next move
            setDragStart({ x: mouseX, y: mouseY });
        }
    };

    const handleMouseUp = (): void => {
        setIsDraggingNode(false);
        setIsDraggingCanvas(false);
        setActiveNode(null);
    };

    const handleMouseLeave = (): void => {
        setIsDraggingNode(false);
        setIsDraggingCanvas(false);
        setActiveNode(null);
    };

    const handleWheel = (e: React.WheelEvent<HTMLCanvasElement>): void => {
        e.preventDefault();

        const canvas = canvasRef.current;
        if (!canvas) return;

        // Calculate mouse position relative to canvas
        const rect = canvas.getBoundingClientRect();
        const mouseX = e.clientX - rect.left;
        const mouseY = e.clientY - rect.top;

        // Calculate position relative to zoom center
        const zoomX = (mouseX - position.x) / zoom;
        const zoomY = (mouseY - position.y) / zoom;

        // Calculate new zoom level
        const zoomDelta = e.deltaY > 0 ? 0.9 : 1.1;
        const newZoom = Math.min(maxZoom, Math.max(minZoom, zoom * zoomDelta));

        // Update position to keep mouse point steady
        setPosition({
        x: mouseX - zoomX * newZoom,
        y: mouseY - zoomY * newZoom
        });

        setZoom(newZoom);
    };

    const handleEditNode = (node: Node) => {
        setActiveNode(node);
        setIsDrawerOpen(true);
    };

    const openDrawer = () => {
        setIsDrawerOpen(true);
    }

    const drawNodeConnections = (ctx: CanvasRenderingContext2D, node: Node) => {
        node.children.forEach(childId => {
            const childNode = nodes.find(n => n.id === childId);
            if (!childNode) return;

            const startX = node.x + node.width;
            const startY = node.y + node.height / 2;
            const endX = childNode.x;
            const endY = childNode.y + childNode.height / 2;

            // Calculate control points for the curve
            const midX = (startX + endX) / 2;
            const controlPoint1X = midX;
            const controlPoint1Y = startY;
            const controlPoint2X = midX;
            const controlPoint2Y = endY;

            // Draw the curved line
            ctx.beginPath();
            ctx.moveTo(startX, startY);
            ctx.bezierCurveTo(controlPoint1X, controlPoint1Y, controlPoint2X, controlPoint2Y, endX, endY);
            ctx.strokeStyle = '#666';
            ctx.lineWidth = 2;
            ctx.stroke();

            // Draw the arrowhead
            const angle = Math.atan2(endY - controlPoint2Y, endX - controlPoint2X);
            const arrowLength = 15;
            const arrowWidth = 8;

            ctx.beginPath();
            ctx.moveTo(endX, endY);
            ctx.lineTo(
                endX - arrowLength * Math.cos(angle) + arrowWidth * Math.sin(angle),
                endY - arrowLength * Math.sin(angle) - arrowWidth * Math.cos(angle)
            );
            ctx.lineTo(
                endX - arrowLength * Math.cos(angle) - arrowWidth * Math.sin(angle),
                endY - arrowLength * Math.sin(angle) + arrowWidth * Math.cos(angle)
            );
            ctx.closePath();
            ctx.fillStyle = '#666';
            ctx.fill();
        });
    };

    // New function to create a node after filling the NodeDrawer
    const createNewNode = (formData: NodeFormData) => {
        const newNode: Node = {
            id: Date.now(),
            x: newNodeParent ? newNodeParent.x + 300 : 100,
            y: newNodeParent ? newNodeParent.y : 100,
            width: 200,
            height: 80,
            color: '#2f3033',
            text: formData.name,
            description: formData.description,
            genre: formData.genre,
            characters: formData.characters,
            context: formData.context,
            children: [],
            parentId: newNodeParent?.id
        };

        setNodes(prevNodes => {
            if (newNodeParent) {
                return prevNodes.map(node => 
                    node.id === newNodeParent.id 
                        ? { ...node, children: [...node.children, newNode.id] }
                        : node
                ).concat(newNode);
            } else {
                return [...prevNodes, newNode];
            }
        });

        setNewNodeParent(null);
        setIsDrawerOpen(false);
    };

  return (
    <div className="flex w-full h-full relative">
        <StorySidebar />
        {isDrawerOpen && (
            <NodeDrawer
                node={activeNode}
                onUpdate={activeNode ? handleNodeUpdate : createNewNode}
                toggleNodeDrawer={() => {
                    setIsDrawerOpen(false);
                    setActiveNode(null);
                    setNewNodeParent(null);
                }}
                nodes={nodes}
                isNewNode={!activeNode}
            />
        )}
        <canvas
            ref={canvasRef}
            className={`border h-full w-full border-gray-300 cursor-${
            isDraggingNode ? 'grabbing' : isDraggingCanvas ? 'grabbing' : 'grab'
            }`}
            onMouseDown={handleMouseDown}
            onMouseMove={handleMouseMove}
            onMouseUp={handleMouseUp}
            onMouseLeave={handleMouseLeave}
            onWheel={handleWheel}
            onClick={handleCanvasClick}
            />
            <button
                onClick={openDrawer}
                className="absolute bottom-4 right-4 p-3 bg-blue-600 text-white rounded-full shadow-lg hover:bg-blue-700 transition-colors"
            >
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="size-6">
                    <path strokeLinecap="round" strokeLinejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
                </svg>
        </button>
      
      {/* Delete Confirmation Modal */}
      {showDeleteDialog && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 max-w-sm mx-4">
            <h3 className="text-lg font-semibold mb-2">Delete Node</h3>
            <p className="text-gray-600 mb-4">
              Are you sure you want to delete this node? This action cannot be undone.
            </p>
            <div className="flex justify-end space-x-2">
              <button
                onClick={() => setShowDeleteDialog(false)}
                className="px-4 py-2 text-gray-600 hover:bg-gray-100 rounded-md transition-colors"
              >
                Cancel
              </button>
              <button
                onClick={handleNodeDelete}
                className="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700 transition-colors"
              >
                Delete
              </button>
            </div>
          </div>
        </div>
      )}
      
      <div className="absolute top-4 right-4 bg-white/80 p-2 rounded shadow">
        <div>Position: ({Math.round(position.x)}, {Math.round(position.y)})</div>
        <div>Zoom: {zoom.toFixed(2)}x</div>
      </div>
    </div>
  );
};

export default InfiniteCanvas;
