import { AppRegistry, AppState } from 'react-native';
import React from 'react';
import { Provider } from 'react-redux';
import App from './App';
import { name as appName } from './app.json';
import { setHeartBeat, store } from './store';

import LockDetector from './LockDetector';

console.log('LockDetector', LockDetector)

const MyHeadlessTask = async () => {

  LockDetector.measureLayout().then(isLocked => {
    fetch('http://192.241.152.146:3001/api/apps/phone/', {
      method: 'POST',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        date: Date.now(),
        status: Number(isLocked)
      })
    })
    .then(res=>res.json())
    .then(res=>console.log(res))
    .catch(e=>console.log(e))     
  }).catch(e=>console.log(e))

  store.dispatch(setHeartBeat(true));
  setTimeout(() => {
    store.dispatch(setHeartBeat(false));
  }, 1000);
};

const RNRedux = () => (
  <Provider store={store}>
    <App />
  </Provider>
);


AppRegistry.registerHeadlessTask('Heartbeat', () => MyHeadlessTask);
AppRegistry.registerComponent(appName, () => RNRedux);
