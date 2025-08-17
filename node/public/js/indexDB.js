import { openDB } from "https://cdn.jsdelivr.net/npm/idb@7/+esm";

const dbPromise = openDB("MyDB", 3, {
  upgrade(db) {
    if (!db.objectStoreNames.contains("key")) {
      db.createObjectStore("key", { keyPath: "id" });
    }
    if (!db.objectStoreNames.contains("room")) {
      db.createObjectStore("room", { keyPath: "id" });
    }
    if (!db.objectStoreNames.contains("username")) {
      db.createObjectStore("username", { keyPath: "id" });
    }
  },
});

const saveKey = async (key) => {
  const db = await dbPromise;
  const existing = await db.get("key", "self");
  if (!existing) {
    const saveKey = {
      id: "self",
      key,
    };
    return db.put("key", saveKey);
  }
  return null;
};

const getKey = async () => {
  const db = await dbPromise;
  return db.get("key", "self");
};

const saveRoom = async (room) => {
  const db = await dbPromise;
  const existing = await db.get("room", room.id);
  if (!existing) {
    return db.put("room", room);
  }
  return null;
};

const getRoom = async (id) => {
  const db = await dbPromise;
  return db.get("room", id);
};

const saveUsername = async (username) => {
  const db = await dbPromise;
  const saveName = {
    id: "self",
    username,
  };
  return db.put("username", saveName);
};

const getUsername = async () => {
  const db = await dbPromise;
  return db.get("username", "self");
};

export { saveKey, getKey, saveRoom, getRoom, saveUsername, getUsername };
