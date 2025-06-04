import {useEffect, useState} from "react";
import {getBackendOptions, MultiBackend, Tree,} from "@minoru/react-dnd-treeview";
import {DndProvider} from "react-dnd";
import PlaceHolder from "./PlaceHolder";
import Command from "./Command";
import {Alert, Box, Button, Container, Dialog, IconButton, Paper, Snackbar, Tooltip, Typography, CircularProgress, Backdrop} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import UploadFileIcon from '@mui/icons-material/UploadFile';
import DownloadIcon from '@mui/icons-material/Download';
import CopyIcon from '@mui/icons-material/FileCopy';
import DoneOutlineIcon from '@mui/icons-material/DoneOutline';
import shortUUID from 'short-uuid';
import './theme.js';
import './global.css';

// 动态插入 Google Fonts 字体
if (typeof document !== 'undefined') {
  const fontLink = document.createElement('link');
  fontLink.rel = 'stylesheet';
  fontLink.href = 'https://fonts.googleapis.com/css2?family=Noto+Sans+SC:wght@400;700&family=Noto+Sans+TC:wght@400;700&family=Noto+Sans+JP:wght@400;700&display=swap';
  document.head.appendChild(fontLink);
}

const DROPPABLE_COMMANDS = ['if', 'elseIf', 'else', 'times', 'section'];

// 公共导出数据函数
function getExportData(treeData) {
  // 修正：同级if/elseIf/else组只用一个end结束
  function flattenIfElseGroup(nodes) {
    let result = [];
    for (const node of nodes) {
      result.push({ ...node.data });
      const children = treeData.filter(n => n.parent === node.id);
      for (const child of children) {
        result = result.concat(flattenCommands(child));
      }
    }
    result.push({ command: 'end', comment: 'end if' });
    return result;
  }
  function flattenCommands(node) {
    let result = [];
    if (!node.data) return result;
    if (node.droppable) {
      // 处理if/elseIf/else组
      if (['if', 'elseIf', 'else'].includes(node.data.command)) {
        // 只处理if为起点的组
        if (node.data.command === 'if') {
          // 找到同级的if/elseIf/else组
          const parent = node.parent;
          const siblings = treeData.filter(n => n.parent === parent);
          const group = [];
          let found = false;
          for (let i = 0; i < siblings.length; i++) {
            if (siblings[i].id === node.id) found = true;
            if (found && siblings[i].data && ['if', 'elseIf', 'else'].includes(siblings[i].data.command)) {
              group.push(siblings[i]);
            } else if (found) {
              break;
            }
          }
          // 只处理一次，跳过组内其他分支
          if (group.length > 0 && group[0].id === node.id) {
            return flattenIfElseGroup(group);
          }
          return [];
        } else {
          // elseIf/else由if分支统一处理，这里跳过
          return [];
        }
      } else {
        // 普通可嵌套命令
        result.push({ ...node.data });
        const children = treeData.filter(n => n.parent === node.id);
        for (const child of children) {
          result = result.concat(flattenCommands(child));
        }
        result.push({ command: 'end', comment: `end ${node.data.command || ''}` });
      }
    } else {
      result.push({ ...node.data });
    }
    return result;
  }
  const rootNodes = treeData.filter(n => n.parent === 0);
  let commandsArray = [];
  for (let i = 0; i < rootNodes.length; i++) {
    commandsArray = commandsArray.concat(flattenCommands(rootNodes[i]));
  }
  return {name: shortUUID.generate(), tests: [{name: shortUUID.generate(), commands: commandsArray}]};
}

const App = () => {
  const [treeData, setTreeData] = useState([]);
  const handleDrop = (newTreeData) => setTreeData(newTreeData);

  const [dialogOpen, setDialogOpen] = useState(false);
  const [editNode, setEditNode] = useState(null); // 当前编辑或新增的节点
  const [editMode, setEditMode] = useState('add'); // 'add' 或 'edit'
  const [formData, setFormData] = useState({ command: '', target: '', value: '', comment: '' });
  const [selectedNodeId, setSelectedNodeId] = useState(null); // 添加选中节点状态

  // 添加提示消息状态
  const [snackbar, setSnackbar] = useState({
    open: false,
    message: '',
    severity: 'success'
  });

  // 新增：执行命令loading状态
  const [isExecuting, setIsExecuting] = useState(false);

  // 关闭提示消息
  const handleCloseSnackbar = () => {
    setSnackbar(prev => ({ ...prev, open: false }));
  };

  // 显示提示消息
  const showMessage = (message, severity = 'success') => {
    setSnackbar({
      open: true,
      message,
      severity
    });
  };

  // 初始化时从localStorage读取treeData
  useEffect(() => {
    const saved = localStorage.getItem('treeData');
    if (saved) {
      try {
        setTreeData(JSON.parse(saved));
      } catch {}
    }
  }, []);

  // treeData变化时保存到localStorage
  useEffect(() => {
    localStorage.setItem('treeData', JSON.stringify(treeData));
  }, [treeData]);

  // 处理键盘快捷键
  useEffect(() => {
    const handleKeyDown = (event) => {
      // 对话框打开时不处理快捷键
      if (dialogOpen) return;

      // Alt + 组合键
      if (event.altKey) {
        // Alt+N: 添加节点
        if (event.key === 'n') {
          event.preventDefault(); // 阻止默认行为

          // 查找当前选中的节点
          const selectedNode = treeData.find(node => node.id === selectedNodeId);

          if (selectedNode && selectedNode.droppable) {
            // 如果有选中节点且该节点可包含子节点，则添加子节点
            openCommandDialog('add', selectedNode);
          } else {
            // 如果没有选中节点或选中的节点不可包含子节点，则添加根节点
            handleAddRootNode();
          }
        }
        // Alt+E: 编辑选中节点
        else if (event.key === 'e') {
          event.preventDefault();

          // 查找当前选中的节点
          const selectedNode = treeData.find(node => node.id === selectedNodeId);

          if (selectedNode) {
            openCommandDialog('edit', selectedNode);
          } else {
            showMessage('请先选择要编辑的命令', 'warning');
          }
        }
        // Alt+D: 删除选中节点
        else if (event.key === 'd') {
          event.preventDefault();

          if (selectedNodeId) {
            handleDeleteNode(selectedNodeId);
          } else {
            showMessage('请先选择要删除的命令', 'warning');
          }
        }
      }
    };

    // 添加事件监听器
    document.addEventListener('keydown', handleKeyDown);

    // 清理函数，移除事件监听器
    return () => {
      document.removeEventListener('keydown', handleKeyDown);
    };
  }, [treeData, selectedNodeId, dialogOpen]); // 依赖项包括treeData, selectedNodeId和dialogOpen

  // 打开新增或编辑弹窗
  const openCommandDialog = (mode, node) => {
    setEditMode(mode);
    setEditNode(node);
    setSelectedNodeId(node?.id || null); // 设置选中节点
    if (mode === 'edit' && node && node.data) {
      setFormData({ ...node.data });
    } else {
      setFormData({ command: '', target: '', value: '', comment: '' });
    }
    setDialogOpen(true);
  };

  // 关闭弹窗
  const handleCancel = () => {
    setDialogOpen(false);
    setEditNode(null);
    setFormData({ command: '', target: '', value: '', comment: '' });
    // 保持选中状态，这样用户关闭对话框后仍然可以看到之前选中的节点
  };

  // 确认操作
  const handleOk = () => {
    if (editMode === 'add' && editNode) {
      // 新增子节点
      const newId = shortUUID.generate();
      const droppable = DROPPABLE_COMMANDS.includes((formData.command || '').toLowerCase()) || DROPPABLE_COMMANDS.includes(formData.command);
      const newNode = {
        id: newId,
        parent: editNode.id,
        text: formData.command || `新节点${newId}`,
        droppable,
        data: { ...formData }
      };
      setTreeData([...treeData, newNode]);
    } else if (editMode === 'edit' && editNode) {
      // 编辑节点
      const droppable = DROPPABLE_COMMANDS.includes((formData.command || '').toLowerCase()) || DROPPABLE_COMMANDS.includes(formData.command);
      setTreeData(treeData.map(n => n.id === editNode.id ? { ...n, data: { ...formData }, text: formData.command || n.text, droppable } : n));
    } else if (editMode === 'add-root') {
      // 新增一级节点
      const newId = shortUUID.generate();
      const droppable = DROPPABLE_COMMANDS.includes((formData.command || '').toLowerCase()) || DROPPABLE_COMMANDS.includes(formData.command);
      setTreeData([
        ...treeData,
        {
          id: newId,
          parent: 0,
          text: formData.command || `新一级节点${newId}`,
          droppable,
          data: { ...formData }
        }
      ]);
    }
    handleCancel();
  };

  // 新增一级节点（弹窗方式）
  const handleAddRootNode = () => {
    setEditMode('add-root');
    setEditNode(null);
    setSelectedNodeId(null); // 清除选中状态
    setFormData({ command: '', target: '', value: '', comment: '' });
    setDialogOpen(true);
  };

  // 处理节点点击
  const handleNodeClick = (node) => {
    setSelectedNodeId(node.id);
  };

  // 删除节点函数
  const handleDeleteNode = (nodeId) => {
    if (!nodeId) return;

    // 找到要删除的节点
    const nodeToDelete = treeData.find(node => node.id === nodeId);
    if (!nodeToDelete) return;

    // 递归查找所有子节点ID（包括子节点的子节点等）
    const findAllChildrenIds = (parentId) => {
      const childrenIds = [];
      const directChildren = treeData.filter(node => node.parent === parentId);

      directChildren.forEach(child => {
        childrenIds.push(child.id);
        childrenIds.push(...findAllChildrenIds(child.id));
      });

      return childrenIds;
    };

    // 获取所有要删除的ID（当前节点及其所有子节点）
    const idsToDelete = [nodeId, ...findAllChildrenIds(nodeId)];

    // 过滤掉要删除的节点
    const newTreeData = treeData.filter(node => !idsToDelete.includes(node.id));

    // 更新数据
    setTreeData(newTreeData);
    setSelectedNodeId(null); // 清除选中状态

    // 显示删除成功消息
    showMessage(`已删除 "${nodeToDelete.text}" 命令${idsToDelete.length > 1 ? '及其子命令' : ''}`, 'success');
  };

  // 校验 if/elseIf/else 语法结构（递归校验所有节点及其子节点）
  // 返回 { error, nodeId }，如无错则返回 null
  function checkIfElseStructure(nodes) {
    let lastType = null;
    for (let i = 0; i < nodes.length; i++) {
      const cmd = nodes[i]?.data?.command;
      if (cmd === 'elseIf') {
        if (lastType !== 'if' && lastType !== 'elseIf') {
          return { error: `elseIf 必须紧跟在 if 或 elseIf 之后 (第${i + 1}个命令)`, nodeId: nodes[i].id };
        }
        lastType = 'elseIf';
      } else if (cmd === 'else') {
        if (lastType !== 'if' && lastType !== 'elseIf') {
          return { error: `else 必须紧跟在 if 或 elseIf 之后 (第${i + 1}个命令)`, nodeId: nodes[i].id };
        }
        lastType = 'else';
      } else if (cmd === 'if') {
        lastType = 'if';
      } else {
        lastType = null;
      }
      // 递归校验子节点
      const children = nodes[i].id ? treeData.filter(n => n.parent === nodes[i].id) : [];
      if (children.length > 0) {
        const err = checkIfElseStructure(children);
        if (err) return err;
      }
    }
    return null;
  }

  return (
    <Container maxWidth="md">
      <Box sx={{ my: 3 }}>
        <Box sx={{ display: 'flex', gap: 1, mb: 2 }}>
          <Button
            variant="outlined"
            startIcon={<AddIcon />}
            onClick={handleAddRootNode}
            size="small"
          >
            新增根节点
          </Button>
          <Button
            variant="outlined"
            startIcon={<UploadFileIcon />}
            component="label"
            size="small"
          >
            导入数据
            <input
              type="file"
              accept="application/json"
              hidden
              onChange={e => {
                const file = e.target.files && e.target.files[0];
                if (!file) return;
                const reader = new FileReader();
                reader.onload = evt => {
                  try {
                    const json = JSON.parse(evt.target.result);
                    if (Array.isArray(json)) {
                      setTreeData(json);
                      showMessage('导入成功', 'success');
                    } else {
                      showMessage('导入的文件格式不正确', 'error');
                    }
                  } catch {
                    showMessage('导入失败，文件内容不是有效的JSON', 'error');
                  }
                };
                reader.readAsText(file);
                e.target.value = '';
              }}
            />
          </Button>
          <Button
            variant="outlined"
            startIcon={<DownloadIcon />}
            size="small"
            onClick={() => {
              const rootNodes = treeData.filter(n => n.parent === 0);
              const err = checkIfElseStructure(rootNodes);
              if (err) {
                setSelectedNodeId(err.nodeId);
                showMessage(err.error, 'error');
                return;
              }
              const blob = new Blob([JSON.stringify(treeData, null, 2)], { type: 'application/json' });
              const url = URL.createObjectURL(blob);
              const a = document.createElement('a');
              a.href = url;
              a.download = 'treeData.json';
              a.click();
              URL.revokeObjectURL(url);
              showMessage('导出成功', 'success');
            }}
          >
            导出数据
          </Button>
          {/* 新增：导出节点data按钮 */}
          <Button
            variant="outlined"
            startIcon={<DownloadIcon />}
            size="small"
            onClick={() => {
              const rootNodes = treeData.filter(n => n.parent === 0);
              const err = checkIfElseStructure(rootNodes);
              if (err) {
                setSelectedNodeId(err.nodeId);
                showMessage(err.error, 'error');
                return;
              }
              const exportData = getExportData(treeData);
              const blob = new Blob([JSON.stringify(exportData, null, 2)], { type: 'application/json' });
              const url = URL.createObjectURL(blob);
              const a = document.createElement('a');
              a.href = url;
              a.download = 'exported-data.json';
              a.click();
              URL.revokeObjectURL(url);
              showMessage('节点data已导出', 'success');
            }}
          >
            导出节点data
          </Button>
          <Button
            variant="outlined"
            startIcon={<DoneOutlineIcon />}
            size="small"
            style={{ marginLeft: 8 }}
            onClick={async () => {
              setIsExecuting(true);
              const exportData = getExportData(treeData);
              try {
                const resp = await fetch('http://localhost:9191/flow', {
                  method: 'POST',
                  headers: { 'Content-Type': 'application/json' },
                  body: JSON.stringify(exportData)
                });
                const result = await resp.json();
                showMessage('命令执行成功: ' + (result.message || '已提交'), 'success');
              } catch (e) {
                showMessage('命令执行失败: ' + (e.message || e), 'error');
              } finally {
                setIsExecuting(false);
              }
            }}
          >
            执行命令
          </Button>
        </Box>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
          <Typography variant="h6">命令树</Typography>
        </Box>

      </Box>

      {treeData.length > 0 && (
        <Paper elevation={2} sx={{ p: 2, mb: 4, borderRadius: 2 }}>
          <DndProvider backend={MultiBackend} options={getBackendOptions()}>
            <Tree
              tree={treeData}
              onDrop={handleDrop}
              rootId={0}
              sort={false}
              insertDroppableFirst={false}
              canDrop={(tree, { dragSource, dropTargetId }) => {
                if (dragSource?.parent === dropTargetId) {
                  return true;
                }
              }}
              initialOpen={true}
              dropTargetOffset={5}
              placeholderRender={(node, { depth }) => (
                <PlaceHolder node={node} depth={depth} />
              )}
              render={(node, { depth, isOpen, onToggle }) => {
                const textDetails = node.data && (node.data.target || node.data.value)
                    ? `（${node.data.target || ''} ${node.data.value || ''}）`
                    : '';
                const text = node.data
                    ? `${node.data.command || ''} ${node.data.comment || ''} ${textDetails}`.trim()
                    : node.text;

                // 判断是否是当前选中的节点
                const isSelected = node.id === selectedNodeId;

                return (
                  <Box
                    onClick={() => handleNodeClick(node)}
                    sx={{
                      display: 'flex',
                      alignItems: 'center',
                      py: 1,
                      pl: depth * 3,
                      pr: 1,
                      my: 0.5,
                      borderRadius: 1,
                      border: isSelected ? '2px solid' : '1px solid',
                      borderColor: isSelected ? 'primary.main' : 'transparent',
                      boxShadow: isSelected ? '0 0 4px rgba(25, 118, 210, 0.5)' : 'none',
                      cursor: 'pointer',
                      transition: 'all 0.2s',
                      '&:hover': {
                        bgcolor: 'action.hover',
                      },
                    }}
                  >
                    {node.droppable && (
                        <span onClick={onToggle}>{isOpen ? "[-]" : "[+]"}</span>
                    )}

                    <Typography
                      sx={{
                        flexGrow: 1,
                        fontWeight: node.droppable ? 'medium' : 'normal',
                        color: isSelected ? 'primary.main' : 'text.primary',
                      }}
                    >
                      {text}
                    </Typography>

                    <Box sx={{ display: 'flex', gap: 0.5 }}>
                      {node.droppable && (
                        <Tooltip title="添加子命令">
                          <IconButton
                            size="small"
                            color="primary"
                            onClick={(e) => {
                              e.stopPropagation(); // 防止触发节点点击事件
                              openCommandDialog('add', node);
                            }}
                          >
                            <AddIcon fontSize="small" />
                          </IconButton>
                        </Tooltip>
                      )}
                      {/* 复制按钮，样式与编辑按钮一致 */}
                      <Tooltip title="复制命令">
                        <IconButton
                          size="small"
                          color="secondary"
                          onClick={e => {
                            e.stopPropagation();
                            // 复制节点逻辑
                            const newId = shortUUID.generate();
                            const copyNode = {
                              ...node,
                              id: newId,
                              text: node.text + ' (复制)',
                              parent: node.parent,
                            };
                            setTreeData([...treeData, copyNode]);
                            showMessage('命令已复制', 'success');
                          }}
                        >
                          <CopyIcon fontSize="small" />
                        </IconButton>
                      </Tooltip>
                      <Tooltip title="编辑命令">
                        <IconButton
                          size="small"
                          color="secondary"
                          onClick={(e) => {
                            e.stopPropagation(); // 防止触发节点点击事件
                            openCommandDialog('edit', node);
                          }}
                        >
                          <EditIcon fontSize="small" />
                        </IconButton>
                      </Tooltip>
                      <Tooltip title="删除命令">
                        <IconButton
                          size="small"
                          sx={{
                            color: theme => theme.palette.text.secondary,
                            border: '1px solid',
                            borderColor: theme => theme.palette.border,
                            backgroundColor: 'transparent',
                            '&:hover': {
                              bgcolor: theme => theme.palette.action.hover,
                              color: theme => theme.palette.text.primary,
                              borderColor: theme => theme.palette.text.primary,
                            }
                          }}
                          onClick={(e) => {
                            e.stopPropagation(); // 防止触发节点点击事件
                            handleDeleteNode(node.id);
                          }}
                        >
                          <DeleteIcon fontSize="small" />
                        </IconButton>
                      </Tooltip>
                    </Box>
                  </Box>
                );
              }}
            />
          </DndProvider>
        </Paper>
      )}

      <Dialog
        open={dialogOpen}
        onClose={handleCancel}
        PaperProps={{
          sx: { borderRadius: 2 }
        }}
      >
        <Command
          command={formData.command}
          target={formData.target}
          value={formData.value}
          comment={formData.comment}
          onChange={(field, val) => setFormData(f => ({ ...f, [field]: val }))}
          onOk={handleOk}
          onCancel={handleCancel}
        />
      </Dialog>

      <Snackbar
        open={snackbar.open}
        autoHideDuration={6000}
        onClose={handleCloseSnackbar}
      >
        <Alert onClose={handleCloseSnackbar} severity={snackbar.severity} sx={{ width: '100%' }}>
          {snackbar.message}
        </Alert>
      </Snackbar>

      {/* 画面全体を覆う実行中ローディング */}
      <Backdrop open={isExecuting} sx={{ color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 2 }}>
        <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          <CircularProgress color="inherit" size={60} thickness={4} />
          <Typography variant="h6" sx={{ mt: 3, letterSpacing: 2 }}>正在执行命令，请稍候...</Typography>
        </Box>
      </Backdrop>
    </Container>
  );
}

export default App;
