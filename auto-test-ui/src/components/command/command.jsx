import { useEffect, useState } from 'react';
import axios from 'axios';
import { Table, Button, Modal, Form } from 'react-bootstrap';
import { API_URL } from "../../util/Constants.jsx";

function Command() {
  const [commands, setCommands] = useState([]);
  const [showEditModal, setShowEditModal] = useState(false);
  const [editCommand, setEditCommand] = useState({});
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [deleteCommand, setDeleteCommand] = useState({});
  const [showAddModal, setShowAddModal] = useState(false);
  const [addCommand, setAddCommand] = useState({});

  useEffect(() => {
    axios.get(API_URL + '/commands')
      .then(response => {
        setCommands(response.data);
      })
      .catch(error => {
        console.log(error);
      });
  }, []);

  const handleAddModalClose = () => {
    setShowAddModal(false);
    setAddCommand({});
  };

  const handleAddModalSave = () => {
    axios.post(API_URL + '/commands', addCommand)
      .then(response => {
        const updatedCommands = [...commands, response.data];
        setCommands(updatedCommands);
        setShowAddModal(false);
        setAddCommand({});
      })
      .catch(error => {
        console.log(error);
      });
  };

  const handleAddCommandChange = (event) => {
    const { name, value } = event.target;
    setAddCommand(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  const handleAddButtonClick = () => {
    setShowAddModal(true);
  };

  const handleEditModalClose = () => {
    setShowEditModal(false);
    setEditCommand({});
  };

  const handleEditModalSave = () => {
    axios.put(API_URL + '/commands/' + editCommand.id, editCommand)
      .then(response => {
        const updatedCommands = commands.map(command => {
          if (command.id === response.data.id) {
            return response.data;
          } else {
            return command;
          }
        });
        setCommands(updatedCommands);
        setShowEditModal(false);
        setEditCommand({});
      })
      .catch(error => {
        console.log(error);
      });
  };

  const handleEditCommandChange = (event) => {
    const { name, value } = event.target;
    setEditCommand(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  const handleEditButtonClick = (command) => {
    setEditCommand(command);
    setShowEditModal(true);
  };

  const handleDeleteModalClose = () => {
    setShowDeleteModal(false);
    setDeleteCommand({});
  };

  const handleDeleteModalSave = () => {
    axios.delete(API_URL + '/commands/' + deleteCommand.id)
      .then(() => {
        const updatedCommands = commands.filter(command => command.id !== deleteCommand.id);
        setCommands(updatedCommands);
        setShowDeleteModal(false);
        setDeleteCommand({});
      })
      .catch(error => {
        console.log(error);
      });
  };

  const handleDeleteButtonClick = (command) => {
    setDeleteCommand(command);
    setShowDeleteModal(true);
  };

  return (
    <>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>ID</th>
            <th>scriptId</th>
            <th>Command</th>
            <th>Target</th>
            <th>Value</th>
            <th>Description</th>
            <th>Edit</th>
            <th>Delete</th>
          </tr>
        </thead>
        <tbody>
          {commands.map(command => (
            <tr key={command.id}>
              <td>{command.id}</td>
              <td>{command.scriptId}</td>
              <td>{command.command}</td>
              <td>{command.target}</td>
              <td>{command.value}</td>
              <td>{command.description}</td>
              <td>
                <Button variant="primary" onClick={() => handleEditButtonClick(command)}>Edit</Button>
              </td>
              <td>
                <Button variant="danger" onClick={() => handleDeleteButtonClick(command)}>Delete</Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      <Button variant="success" onClick={handleAddButtonClick}>Add</Button>

      <Modal show={showAddModal} onHide={handleAddModalClose}>
        <Modal.Header closeButton>
          <Modal.Title>Add Command</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group controlId="formScriptId">
              <Form.Label>Script ID</Form.Label>
              <Form.Control type="text" name="scriptId" value={addCommand.scriptId || ''} onChange={handleAddCommandChange} />
            </Form.Group>
            <Form.Group controlId="formCommand">
              <Form.Label>Command</Form.Label>
              <Form.Control type="text" name="command" value={addCommand.command || ''} onChange={handleAddCommandChange} />
            </Form.Group>
            <Form.Group controlId="formTarget">
              <Form.Label>Target</Form.Label>
              <Form.Control type="text" name="target" value={addCommand.target || ''} onChange={handleAddCommandChange} />
            </Form.Group>
            <Form.Group controlId="formValue">
              <Form.Label>Value</Form.Label>
              <Form.Control type="text" name="value" value={addCommand.value || ''} onChange={handleAddCommandChange} />
            </Form.Group>
            <Form.Group controlId="formDescription">
              <Form.Label>Description</Form.Label>
              <Form.Control type="text" name="description" value={addCommand.description || ''} onChange={handleAddCommandChange} />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleAddModalClose}>
            Close
          </Button>
          <Button variant="primary" onClick={handleAddModalSave}>
            Save Changes
          </Button>
        </Modal.Footer>
      </Modal>

      <Modal show={showEditModal} onHide={handleEditModalClose}>
        <Modal.Header closeButton>
          <Modal.Title>Edit Command</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group controlId="formScriptId">
              <Form.Label>Script ID</Form.Label>
              <Form.Control type="text" name="scriptId" value={editCommand.scriptId || ''} onChange={handleEditCommandChange} />
            </Form.Group>
            <Form.Group controlId="formCommand">
              <Form.Label>Command</Form.Label>
              <Form.Control type="text" name="command" value={editCommand.command || ''} onChange={handleEditCommandChange} />
            </Form.Group>
            <Form.Group controlId="formTarget">
              <Form.Label>Target</Form.Label>
              <Form.Control type="text" name="target" value={editCommand.target || ''} onChange={handleEditCommandChange} />
            </Form.Group>
            <Form.Group controlId="formValue">
              <Form.Label>Value</Form.Label>
              <Form.Control type="text" name="value" value={editCommand.value || ''} onChange={handleEditCommandChange} />
            </Form.Group>
            <Form.Group controlId="formDescription">
              <Form.Label>Description</Form.Label>
              <Form.Control type="text" name="description" value={editCommand.description || ''} onChange={handleEditCommandChange} />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleEditModalClose}>
            Close
          </Button>
          <Button variant="primary" onClick={handleEditModalSave}>
            Save Changes
          </Button>
        </Modal.Footer>
      </Modal>

      <Modal show={showDeleteModal} onHide={handleDeleteModalClose}>
        <Modal.Header closeButton>
          <Modal.Title>Delete Command</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          Are you sure you want to delete this command?
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleDeleteModalClose}>
            Cancel
          </Button>
          <Button variant="danger" onClick={handleDeleteModalSave}>
            Delete
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}
export default Command
