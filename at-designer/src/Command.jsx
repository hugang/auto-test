// Command.jsx
// A simple React component for command display or input
import React, {useEffect} from 'react';
import {
    TextField,
    Button,
    Box,
    Typography,
    Stack,
    Paper,
    Autocomplete
} from '@mui/material';

// 预定义的命令列表
const commonCommands = [
    'if',
    'elseIf', // 追加
    'else',   // 追加
    'times',
    'section',
    'click',
    'type',
    'wait',
    'open',
    'setProperty',
    'setWindowSize',
    'select',
    'scroll',
];

// 命令对应的提示信息映射
const commandHints = {
    if: '执行条件判断，如果满足条件则执行。',
    elseIf: '条件判断，如果前面的 if 条件不满足，则执行。',
    else: '如果前面的条件都不满足时执行。',
    times: '重复执行指定次数。',
    section: '定义一个步骤块。',
    click: '点击指定元素。',
    type: '在指定元素输入文本。',
    wait: '等待指定时间。目标或者值：等待时间毫秒”',
    open: '打开指定 URL。',
    setProperty: '设置元素属性。',
    setWindowSize: '调整浏览器窗口大小。',
    select: '在下拉框中选择值。',
    scroll: '滚动页面至指定位置。',
    log: '记录画面信息到报告。不需要目标或值。',
};

const Command = ({command, target, value, comment, onChange, onOk, onCancel}) => {
    // 添加键盘事件处理
    useEffect(() => {
        const handleKeyDown = (event) => {
            // Alt+Enter组合键
            if (event.altKey && event.key === 'Enter') {
                onOk();
            }
        };

        // 添加事件监听器
        document.addEventListener('keydown', handleKeyDown);

        // 清理函数，移除事件监听器
        return () => {
            document.removeEventListener('keydown', handleKeyDown);
        };
    }, [onOk]); // 依赖项包括onOk函数

    // 当前命令提示
    const hint = commandHints[command];

    return (
        <Paper elevation={0} sx={{padding: 3, minWidth: 350}}>
            <Typography variant="h6" mb={2} color="primary">
                编辑命令
            </Typography>
            <Stack spacing={2}>
                <Autocomplete
                    freeSolo
                    options={commonCommands}
                    value={command || ''}
                    onChange={(event, newValue) => onChange('command', newValue)}
                    inputValue={command || ''}
                    onInputChange={(event, newInputValue) => onChange('command', newInputValue)}
                    renderInput={(params) => (
                        <TextField
                            {...params}
                            fullWidth
                            label="命令"
                            variant="outlined"
                            size="small"
                            helperText="选择或输入一个命令"
                        />
                    )}
                />
                {hint && (
                    <Typography variant="caption" color="text.secondary">
                        {hint}
                    </Typography>
                )}
                <TextField
                    fullWidth
                    label="目标"
                    variant="outlined"
                    size="small"
                    value={target}
                    onChange={e => onChange('target', e.target.value)}
                    multiline
                    minRows={3}
                />
                <TextField
                    fullWidth
                    label="值"
                    variant="outlined"
                    size="small"
                    value={value}
                    onChange={e => onChange('value', e.target.value)}
                    multiline
                    minRows={3}
                />
                <TextField
                    fullWidth
                    label="注释"
                    variant="outlined"
                    size="small"
                    value={comment}
                    onChange={e => onChange('comment', e.target.value)}
                />
                <Box sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center', mt: 1}}>
                    <Typography variant="caption" color="text.secondary">
                        提示: 按下 Alt+Enter 快速提交
                    </Typography>
                    <Box sx={{display: 'flex', gap: 1}}>
                        <Button variant="outlined" onClick={onCancel}>
                            取消
                        </Button>
                        <Button variant="contained" color="primary" onClick={onOk}>
                            确定
                        </Button>
                    </Box>
                </Box>
            </Stack>
        </Paper>
    );
};

export default Command;
