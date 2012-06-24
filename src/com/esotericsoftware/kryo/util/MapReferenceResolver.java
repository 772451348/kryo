
package com.esotericsoftware.kryo.util;

import java.util.ArrayList;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.ReferenceResolver;

/** Uses an {@link IdentityObjectIntMap} to track objects that have already been written. This can handle graph with any number of
 * objects, but is slightly slower than {@link ListReferenceResolver} for graphs with few objects.
 * @author Nathan Sweet <misc@n4te.com> */
public class MapReferenceResolver implements ReferenceResolver {
	protected Kryo kryo;
	protected final IdentityObjectIntMap writtenObjects = new IdentityObjectIntMap();
	protected final ArrayList readObjects = new ArrayList();

	public void setKryo (Kryo kryo) {
		this.kryo = kryo;
	}

	public void addWrittenObject (int id, Object object) {
		writtenObjects.put(object, id);
	}

	public int getWrittenId (Object object) {
		return writtenObjects.get(object, -1);
	}

	public void addReadObject (int id, Object object) {
		if (id == readObjects.size())
			readObjects.add(object);
		else {
			while (id >= readObjects.size())
				readObjects.add(null);
			readObjects.set(id, object);
		}
	}

	public Object getReadObject (int id) {
		if (id < readObjects.size()) return readObjects.get(id);
		return null;
	}

	public void reset () {
		readObjects.clear();
		writtenObjects.clear();
	}

	/** Returns false for all primitive wrappers. */
	public boolean useReferences (Class type) {
		return !Util.isWrapperClass(type);
	}
}