import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import styled from 'styled-components';
import { getUsers } from '../api/users-axios';
import LoadingWrapper from '../components/LoadingWrapper';

const UserContainer = styled.div`
  border: 1px solid #ccc;
  padding: 10px;
  margin: 10px;
  border-radius: 8px;
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const Username = styled.h1`
  margin: 0;
`;

const ActionButtons = styled.div`
  display: flex;
  gap: 10px;
`;

export default function AdminPanel() {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleDelete = () => {
    console.log('delete');
  };

  useEffect(() => {
    setLoading(true);
    getUsers()
      .then((results) => {
        setUsers(results);
      })
      .catch((error) => {
        setError(true);
        console.error(error);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  return (
    <LoadingWrapper loading={loading} error={error}>
      {users.map((user) => (
        <UserContainer key={user.id}>
          <Username>{user.username}</Username>
          <ActionButtons>
            <Link to={`/admin/edit/${user.id}`}>
              <button>Edit</button>
            </Link>
            <button onClick={() => handleDelete(user.id)}>Delete</button>
          </ActionButtons>
        </UserContainer>
      ))}
    </LoadingWrapper>
  );
}
